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

import com.practica.carzone.model.Financiacion;
import com.practica.carzone.model.Usuario;
import com.practica.carzone.repository.FinanciacionRepository;
import com.practica.carzone.repository.UsuarioRepository;

@Controller
@RequestMapping("/financiaciones")
public class financiacionController {
    
    private Logger logger = LoggerFactory.getLogger(financiacionController.class);

    @Autowired
    private FinanciacionRepository financiacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listaFinanciaciones(Model model) {

        List<Financiacion> listaFinanciaciones = financiacionRepository.findAll();

        model.addAttribute("financiaciones", listaFinanciaciones);
        
        return "secundarias/financiacion/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeFinanciacion(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!financiacionRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "La financiacion no existe");
        else {
            financiacionRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente la financiacion con el id " + id);
            logger.info("Se ha borrado correctamente la financiacion con el id " + id);
        }

        return "redirect:/financiaciones";
    }

    @GetMapping("/nuevo")
    public String newFinanciacion(Model model) {

        Financiacion financiacion = new Financiacion();

        List<Usuario> listaUsuario = usuarioRepository.findAll();

        model.addAttribute("financiacion", financiacion);

        model.addAttribute("usuarios", listaUsuario);

        return "secundarias/financiacion/nuevo";
    }

    @PostMapping("/crear")
    public String createFinanciacion(@ModelAttribute("financiacion") Financiacion financiacion) {
        
        financiacionRepository.save(financiacion);

        logger.info("Se ha creado la financiacion con el id " + financiacion.getId());

        return "redirect:/financiaciones";
    }
    
    @GetMapping("/editar/{id}")
    public String editFinanciacion(@PathVariable Long id, Model model) {

        Financiacion financiacion = new Financiacion();

        List<Usuario> listaUsuario = usuarioRepository.findAll();

        if (!financiacionRepository.existsById(id)) {
            model.addAttribute("error", "La financiacion no Existe");
            financiacion = financiacionRepository.findById(id).get();
        }

        model.addAttribute("financiacion", financiacion);

        model.addAttribute("usuarios", listaUsuario);

        return "secundarias/financiacion/editar";
    }

    @PostMapping("/modificar")
    public String modifyFinanciacion(@ModelAttribute("financiacion") Financiacion financiacion, Model model) {

        try {
            financiacionRepository.save(financiacion); 
        } catch(Exception e) {
            model.addAttribute("error", "La financiacion no Existe");
        }

        return "redirect:/financiaciones";
    }

}
