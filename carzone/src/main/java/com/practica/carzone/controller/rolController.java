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
import com.practica.carzone.repository.RolRepository;

@Controller
@RequestMapping("/roles")
public class rolController {
    
    private Logger logger = LoggerFactory.getLogger(rolController.class);

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public String listaRoles(Model model) {

        List<Rol> listaRoles = rolRepository.findAll();

        model.addAttribute("roles", listaRoles);
        
        return "secundarias/rol/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeRol(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!rolRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "El rol no existe");
        else {
            rolRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente el rol con el id " + id);
            logger.info("Se ha borrado correctamente el rol con el id " + id);
        }

        return "redirect:/roles";
    }

    @GetMapping("/nuevo")
    public String newRol(Model model) {

        Rol rol = new Rol();

        model.addAttribute("rol", rol);

        return "secundarias/rol/nuevo";
    }

    @PostMapping("/crear")
    public String createRol(@ModelAttribute("rol") Rol rol) {
        
        rolRepository.save(rol);

        logger.info("Se ha creado el rol con el id " + rol.getId());

        return "redirect:/roles";
    }
    
    @GetMapping("/editar/{id}")
    public String editRol(@PathVariable Long id, Model model) {

        Rol rol = new Rol();

        if (!rolRepository.existsById(id))
            model.addAttribute("error", "El rol no Existe");
            rol = rolRepository.findById(id).get();

        model.addAttribute("rol", rol);

        return "secundarias/rol/editar";
    }

    @PostMapping("/modificar")
    public String modifyRol(@ModelAttribute("rol") Rol rol, Model model) {

        try {
            rolRepository.save(rol); 
        } catch(Exception e) {
            model.addAttribute("error", "El rol no Existe");
        }

        return "redirect:/roles";
    }

}
