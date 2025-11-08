package com.practica.carzone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.practica.carzone.model.Coche;
import com.practica.carzone.model.Marca;
import com.practica.carzone.repository.CocheRepository;
import com.practica.carzone.repository.MarcaRepository;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/coches")
public class cocheController {
    
    private Logger logger = LoggerFactory.getLogger(cocheController.class);

    @Autowired
    private CocheRepository cocheRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @GetMapping
    public String listaCoches(Model model) {
        List<Coche> listaCoches = cocheRepository.findAll();

        model.addAttribute("coches", listaCoches);

        return "principales/coche/lista";
    }
    
    @GetMapping("/eliminar/{id}")
    public String removeCoche(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!cocheRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "el coche no existe");
        else {
            cocheRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente el coche con el id " + id);
            logger.info("Se ha borrado correctamente el coche con el id " + id);
        }

        return "redirect:/coches";
    }

    @GetMapping("/nuevo")
    public String newCoche(Model model) {
        Coche coche = new Coche();

        List<Marca> listaMarcas = marcaRepository.findAll();

        model.addAttribute("coche", coche);

        model.addAttribute("marcas", listaMarcas);

        return "principales/coche/nuevo";
    }

    @PostMapping("/crear")
    public String createCoche(@ModelAttribute("coche") Coche coche) {
        
        cocheRepository.save(coche);

        logger.info("Se ha creado el coche con el id " + coche.getId());

        return "redirect:/coches";
    }
    
    @GetMapping("/editar/{id}")
    public String editCoche(@PathVariable Long id, Model model) {

        Coche coche = new Coche();

        List<Marca> listaMarcas= marcaRepository.findAll();

        if (!cocheRepository.existsById(id)){
            model.addAttribute("error", "El coche no Existe");
            coche = cocheRepository.findById(id).get();
        }

        model.addAttribute("coche", coche);

        model.addAttribute("marcas", listaMarcas);

        return "principales/coche/editar";
    }

    @PostMapping("/modificar")
    public String modifyCoche(@ModelAttribute("coche") Coche coche, Model model) {

        try {
            cocheRepository.save(coche); 
        } catch(Exception e) {
            model.addAttribute("error", "El coche no Existe");
        }

        return "redirect:/coches";
    }

}
