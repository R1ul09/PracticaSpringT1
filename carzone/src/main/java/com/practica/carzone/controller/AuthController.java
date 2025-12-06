package com.practica.carzone.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AuthController {

    /**
     * Muestra la página de login
     *
     * @return el nombre de la plantilla `login`
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Muestra el dashboard y añade información del usuario autenticado al
     * modelo para que las vistas (p. ej. `dashboard.html`) puedan mostrar el
     * nombre y el email
     *
     * @param oauth2User principal OAuth2 inyectado por Spring Security
     * @param model      modelo Thymeleaf para pasar atributos a la vista
     * @return el nombre de la plantilla `dashboard`
     */
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        if (oauth2User != null) {
            String nombre = oauth2User.getAttribute("name");
            String email = oauth2User.getAttribute("email");
            model.addAttribute("userName", nombre != null ? nombre : email);
            model.addAttribute("userEmail", email);
        }
        return "dashboard";
    }

}
