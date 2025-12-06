package com.practica.carzone.security;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.practica.carzone.model.Rol;
import com.practica.carzone.model.Usuario;
import com.practica.carzone.repository.RolRepository;
import com.practica.carzone.repository.UsuarioRepository;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = delegate.loadUser(userRequest);

        try {
            Map<String, Object> attributes = oauthUser.getAttributes();

            // Intentamos obtener email y nombre de forma genérica
            String email = (String) attributes.get("email");
            String nombre = (String) attributes.get("name");
            String givenName = (String) attributes.get("given_name");
            String familyName = (String) attributes.get("family_name");

            if (nombre == null && givenName != null) {
                nombre = givenName + (familyName != null ? " " + familyName : "");
            }

            if (email == null) {
                logger.warn("OAuth2 provider did not return an email for user: {}", attributes);
            } else {
                Optional<Usuario> existing = usuarioRepository.findByEmail(email);
                Usuario user;
                if (existing.isPresent()) {
                    user = existing.get();
                    if (nombre != null) {
                        // intentar separar nombre y apellidos si procede
                        String[] parts = nombre.split(" ", 2);
                        user.setNombre(parts[0]);
                        if (parts.length > 1) user.setApellidos(parts[1]);
                    }
                    usuarioRepository.save(user);
                    logger.info("Usuario OAuth2 actualizado: {}", email);
                } else {
                    user = new Usuario();
                    if (nombre != null) {
                        String[] parts = nombre.split(" ", 2);
                        user.setNombre(parts[0]);
                        if (parts.length > 1) user.setApellidos(parts[1]);
                    }
                    user.setEmail(email);
                    user.setContrasena("");

                    // asignar un rol por defecto si existe, o crear uno
                    Rol rol = rolRepository.findAll().stream().findFirst().orElseGet(() -> {
                        Rol r = new Rol();
                        r.setNombreRol("USER");
                        return rolRepository.save(r);
                    });

                    user.setRol(rol);

                    usuarioRepository.save(user);
                    logger.info("Usuario OAuth2 creado: {}", email);
                }
            }

        } catch (Exception e) {
            logger.error("Error mapeando usuario OAuth2", e);
        }

        return oauthUser;
    }

}
