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

import com.practica.carzone.model.Comparativa;
import com.practica.carzone.repository.ComparativaRepository;


@Controller
@RequestMapping("/comparativas")
public class comparativaController {
    
    private Logger logger = LoggerFactory.getLogger(comparativaController.class);

    @Autowired
    private ComparativaRepository comparativaRepository;

    @GetMapping
    public String listaComparativas(Model model) {

        List<Comparativa> listaComparativas = comparativaRepository.findAll();

        model.addAttribute("comparativas", listaComparativas);
        
        return "secundarias/comparativa/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeComparativa(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!comparativaRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "la comparativa no existe");
        else {
            comparativaRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente la comparativa con el id " + id);
            logger.info("Se ha borrado correctamente la comparativa con el id " + id);
        }

        return "redirect:/citas";
    }

    @GetMapping("/nuevo")
    public String newComparativa(Model model) {

        Comparativa comparativa = new Comparativa();

        model.addAttribute("comparativa", comparativa);

        return "secundarias/comparativa/nuevo";
    }

    @PostMapping("/crear")
    public String createComparativa(@ModelAttribute("comparativa") Comparativa comparativa) {
        
        comparativaRepository.save(comparativa);

        logger.info("Se ha creado la comparativa con el id " + comparativa.getId());

        return "redirect:/comparativas";
    }
    
    @GetMapping("/editar/{id}")
    public String editComparativa(@PathVariable Long id, Model model) {

        Comparativa comparativa = new Comparativa();

        if (!comparativaRepository.existsById(id)) {
            model.addAttribute("error", "La comparativa no Existe");
        } else {
            comparativa = comparativaRepository.findById(id).get();
        }
            
        model.addAttribute("comparativa", comparativa);

        return "secundarias/comparativa/editar";
    }

    @PostMapping("/modificar")
    public String modifyComparativa(@ModelAttribute("comparativa") Comparativa comparativa, Model model) {

        try {
            comparativaRepository.save(comparativa); 
        } catch(Exception e) {
            model.addAttribute("error", "La comparativa no Existe");
        }

        return "redirect:/comparativas";
    }
    
}