package com.practica.carzone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practica.carzone.model.Rol;
import com.practica.carzone.model.Usuario;
import com.practica.carzone.repository.RolRepository;
import com.practica.carzone.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class usuarioController {
    
    private Logger logger = LoggerFactory.getLogger(usuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public String listaUsuarios(Model model) {

        List<Usuario> listaUsuarios = usuarioRepository.findAll();

        model.addAttribute("usuarios", listaUsuarios);
        
        return "secundarias/usuario/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeUsuario(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!usuarioRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "el usuario no existe");
        else {
            usuarioRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente el usuario con el id " + id);
            logger.info("Se ha borrado correctamente el usuario con el id " + id);
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/nuevo")
    public String newUsuario(Model model) {

        Usuario usuario = new Usuario();

        List<Rol> listaRoles = rolRepository.findAll();

        model.addAttribute("usuario", usuario);

        model.addAttribute("roles", listaRoles);

        return "secundarias/usuario/nuevo";
    }

    @PostMapping("/crear")
    public String createUsuario(@ModelAttribute("usuario") Usuario usuario) {
        
        usuarioRepository.save(usuario);

        logger.info("Se ha creado el usuario con el id " + usuario.getId());

        return "redirect:/usuarios";
    }
    
    @GetMapping("/editar/{id}")
    public String editUsuario(@PathVariable Long id, Model model) {

        Usuario usuario = new Usuario();

        List<Rol> listaRoles = rolRepository.findAll();

        if (!usuarioRepository.existsById(id)) {
            model.addAttribute("error", "el usuario no Existe");
            usuario = usuarioRepository.findById(id).get();
        }
        
        model.addAttribute("usuario", usuario);

        model.addAttribute("roles", listaRoles);

        return "secundarias/usuario/editar";
    }

    @PostMapping("/modificar")
    public String modifyUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {

        try {
            usuarioRepository.save(usuario); 
        } catch(Exception e) {
            model.addAttribute("error", "el usuario no Existe");
        }

        return "redirect:/usuarios";
    }

}
