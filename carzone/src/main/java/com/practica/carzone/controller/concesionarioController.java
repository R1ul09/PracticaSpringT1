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

import com.practica.carzone.model.Concesionario;
import com.practica.carzone.repository.ConcesionarioRepository;

@Controller
@RequestMapping("/concesionarios")
public class concesionarioController {
    
    private Logger logger = LoggerFactory.getLogger(concesionarioController.class);

    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    @GetMapping
    public String listaConcesionarios(Model model) {

        List<Concesionario> listaConcesionarios = concesionarioRepository.findAll();

        model.addAttribute("concesionarios", listaConcesionarios);
        
        return "secundarias/concesionario/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeConcesionario(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!concesionarioRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "El concesionario no existe");
        else {
            concesionarioRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente el concesionario con el id " + id);
            logger.info("Se ha borrado correctamente el concesionario con el id " + id);
        }

        return "redirect:/concesionarios";
    }

    @GetMapping("/nuevo")
    public String newConcesionario(Model model) {

        Concesionario concesionario = new Concesionario();

        model.addAttribute("concesionario", concesionario);

        return "secundarias/concesionario/nuevo";
    }

    @PostMapping("/crear")
    public String createConcesionario(@ModelAttribute("concesionario") Concesionario concesionario) {
        
        concesionarioRepository.save(concesionario);

        logger.info("Se ha creado el concesionario con el id " + concesionario.getId());

        return "redirect:/concesionarios";
    }
    
    @GetMapping("/editar/{id}")
    public String editConcesionario(@PathVariable Long id, Model model) {

        Concesionario concesionario = new Concesionario();

        if (!concesionarioRepository.existsById(id)) {
            model.addAttribute("error", "El concesionario no Existe");
        } else {
            concesionario = concesionarioRepository.findById(id).get();
        }

        model.addAttribute("concesionario", concesionario);

        return "secundarias/concesionario/editar";
    }

    @PostMapping("/modificar")
    public String modifyConcesionario(@ModelAttribute("concesionario") Concesionario concesionario, Model model) {

        try {
            concesionarioRepository.save(concesionario); 
        } catch(Exception e) {
            model.addAttribute("error", "El concesionario no Existe");
        }

        return "redirect:/concesionarios";
    }
    
}
