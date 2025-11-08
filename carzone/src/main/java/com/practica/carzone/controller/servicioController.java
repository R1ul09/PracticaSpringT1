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

import com.practica.carzone.model.Servicio;
import com.practica.carzone.repository.ServicioRepository;

@Controller
@RequestMapping("/servicios")
public class servicioController {
    
    private Logger logger = LoggerFactory.getLogger(servicioController.class);

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping
    public String listaServicios(Model model) {

        List<Servicio> listaServicios = servicioRepository.findAll();

        model.addAttribute("servicios", listaServicios);
        
        return "secundarias/servicio/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeServicio(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!servicioRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "El servicio no existe");
        else {
            servicioRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente el servicio con el id " + id);
            logger.info("Se ha borrado correctamente el servicio con el id " + id);
        }

        return "redirect:/servicios";
    }

    @GetMapping("/nuevo")
    public String newServicio(Model model) {

        Servicio servicio = new Servicio();

        model.addAttribute("servicio", servicio);

        return "secundarias/servicio/nuevo";
    }

    @PostMapping("/crear")
    public String createServicio(@ModelAttribute("servicio") Servicio servicio) {
        
        servicioRepository.save(servicio);

        logger.info("Se ha creado el servicio con el id " + servicio.getId());

        return "redirect:/servicios";
    }
    
    @GetMapping("/editar/{id}")
    public String editServicio(@PathVariable Long id, Model model) {

        Servicio servicio = new Servicio();

        if (!servicioRepository.existsById(id))
            model.addAttribute("error", "El servicio no Existe");
            servicio = servicioRepository.findById(id).get();

        model.addAttribute("servicio", servicio);

        return "secundarias/servicio/editar";
    }

    @PostMapping("/modificar")
    public String modifyServicio(@ModelAttribute("servicio") Servicio servicio, Model model) {

        try {
            servicioRepository.save(servicio); 
        } catch(Exception e) {
            model.addAttribute("error", "El servicio no Existe");
        }

        return "redirect:/servicios";
    }

}
