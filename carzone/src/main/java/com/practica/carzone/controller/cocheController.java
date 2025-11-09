package com.practica.carzone.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/eliminarCoche/{marcaId}/{cocheId}")
    public String removeCocheVistaEdit(@PathVariable Long marcaId, @PathVariable Long cocheId, RedirectAttributes redAttrib) {
    
        if (!cocheRepository.existsById(cocheId))
            redAttrib.addFlashAttribute("error", "el coche no existe");
        else {
            cocheRepository.deleteById(cocheId);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente el coche con el id " + cocheId);
            logger.info("Se ha borrado correctamente el coche con el id " + cocheId);
        }

        return "redirect:/marcas/ver/{marcaId}";
    }
    

    @GetMapping("/nuevo")
    public String newCoche( @RequestParam(required = false) String redirectTarget, Model model) {
        Coche coche = new Coche();

        List<Marca> listaMarcas = marcaRepository.findAll();

        model.addAttribute("coche", coche);

        String targetValue;

        if (redirectTarget == null) {
            targetValue = "lista";
        } else {
            targetValue = redirectTarget;
        }

        model.addAttribute("redirectTarget", targetValue);

        model.addAttribute("marcas", listaMarcas);

        return "principales/coche/nuevo";
    }

    @PostMapping("/crear")
    public String createCoche(@ModelAttribute("coche") Coche coche, @RequestParam(required = false) String redirectTarget) {
        
        Long marcaId = coche.getMarca().getId();

        cocheRepository.save(coche);

        logger.info("Se ha creado el coche con el id " + coche.getId());

        if ("detalle".equals(redirectTarget)) {
            return "redirect:/marcas/ver/" + marcaId;
        } else {
            return "redirect:/coches";
        }
    }
    
    @GetMapping("/editar/{id}")
    public String editCoche(@PathVariable Long id, @RequestParam(required = false) String redirectTarget, Model model) {

        Coche coche = new Coche();

        List<Marca> listaMarcas= marcaRepository.findAll();

        if (!cocheRepository.existsById(id)){
            model.addAttribute("error", "El coche no Existe");
        } else {
            coche = cocheRepository.findById(id).get();
        }

        model.addAttribute("coche", coche);

        String targetValue;

        if (redirectTarget == null) {
            targetValue = "lista";
        } else {
            targetValue = redirectTarget;
        }
        
        model.addAttribute("redirectTarget", targetValue);

        model.addAttribute("marcas", listaMarcas);

        return "principales/coche/editar";
    }

    @PostMapping("/modificar")
    public String modifyCoche(@ModelAttribute("coche") Coche coche, @RequestParam(required = false) String redirectTarget, Model model) {

        Long marcaId = coche.getMarca().getId();
        
        try {
            cocheRepository.save(coche); 
        } catch(Exception e) {
            model.addAttribute("error", "El coche no Existe");
        }

        if ("detalle".equals(redirectTarget)) {
            return "redirect:/marcas/ver/" + marcaId;
        } else {
            return "redirect:/coches";
        }
    }

    @GetMapping("/desasociar/{id}")
    public String desasociarCochedeMarca(@PathVariable Long id, RedirectAttributes redAttrib) {

        if (!cocheRepository.existsById(id)) {
            redAttrib.addFlashAttribute("error", "el coche no existe");
            return "redirect:/coches";
        }

        Coche coche = cocheRepository.findById(id).get();

        if (coche.getMarca() == null) {
            redAttrib.addFlashAttribute("warning", "El coche no esta asociado a ninguna marca");
            return "redirect:/coches";
        }

        coche.setMarca(null);
        cocheRepository.save(coche);
        redAttrib.addFlashAttribute("success", "Se ha desasociado correctamente la marca del coche con el id " + id);
        logger.info("Se ha desasociado correctamente la marca del coche con el id " + id);
        
        return "redirect:/coches";

    }

    @GetMapping("/asociar/{id}")
    public String asociarCocheAMarca(@PathVariable Long id, Model model, RedirectAttributes redAttrib) {

        if (!cocheRepository.existsById(id)){
            model.addAttribute("error", "El coche no Existe");
            redAttrib.addFlashAttribute("error", "El coche no existe");
            return "redirect:/coches";
        } 

        Coche coche = cocheRepository.findById(id).get();

        if (coche.getMarca() != null) {
            redAttrib.addFlashAttribute("warning", "Este coche ya esta asociado a una marca");
            return "redirect:/coches";
        } 

        List<Marca> listaMarcas= marcaRepository.findAll();

        model.addAttribute("coche", coche);

        model.addAttribute("marcas", listaMarcas);

        return "fragments/asociar";
    }

    @PostMapping("/asociar/guardar")
    public String modifyCoche(@ModelAttribute("coche") Coche coche, Model model) {
        
        try {
            cocheRepository.save(coche); 
        } catch(Exception e) {
            model.addAttribute("error", "El coche no Existe");
        }

        return "redirect:/coches";
    }
    

}
