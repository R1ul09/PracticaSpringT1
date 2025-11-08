package com.practica.carzone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.practica.carzone.model.Coche;
import com.practica.carzone.model.Marca;
import com.practica.carzone.repository.CocheRepository;
import com.practica.carzone.repository.MarcaRepository;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/marcas")
public class marcaController {
    
    private Logger logger = LoggerFactory.getLogger(marcaController.class);

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CocheRepository cocheRepository;

    @GetMapping
    public String listaMarcas(Model model) {

        List<Marca> listaMarcas = marcaRepository.findAll();

        model.addAttribute("marcas", listaMarcas);
        
        return "principales/marca/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeMarca(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!marcaRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "la marca no existe");
        else {
            marcaRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente la marca con el id " + id);
            logger.info("Se ha borrado correctamente la marca con el id " + id);
        }

        return "redirect:/marcas";
    }

    @GetMapping("/nuevo")
    public String newMarca(Model model) {

        Marca marca = new Marca();

        model.addAttribute("marca", marca);

        return "principales/marca/nuevo";
    }

    @PostMapping("/crear")
    public String createMarca(@ModelAttribute("marca") Marca marca) {
        
        marcaRepository.save(marca);

        logger.info("Se ha creado la marca con el id " + marca.getId());

        return "redirect:/marcas";
    }
    
    @GetMapping("/editar/{id}")
    public String editMarca(@PathVariable Long id, Model model) {

        Marca marca = new Marca();

        if (!marcaRepository.existsById(id)) {
            model.addAttribute("error", "La marca no Existe");
        } else {
            marca = marcaRepository.findById(id).get();
        }

        model.addAttribute("marca", marca);

        return "principales/marca/editar";
    }

    @PostMapping("/modificar")
    public String modifyMarca(@ModelAttribute("marca") Marca marca, Model model) {

        try {
            marcaRepository.save(marca); 
        } catch(Exception e) {
            model.addAttribute("error", "La marca no Existe");
        }

        return "redirect:/marcas";
    }

    @GetMapping("/ver/{id}")
    public String VerMarca(@PathVariable Long id, Model model) {

        Marca marca = new Marca();

        List<Coche> listaCoches = cocheRepository.findAll();

        if (!marcaRepository.existsById(id))
            model.addAttribute("error", "La marca no Existe");
        else
            marca = marcaRepository.findById(id).get();

        model.addAttribute("marca", marca);

        model.addAttribute("coches", listaCoches);

        return "principales/marca/detalle";
    }
    
}
