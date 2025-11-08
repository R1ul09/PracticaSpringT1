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

import com.practica.carzone.model.Cita;
import com.practica.carzone.model.Concesionario;
import com.practica.carzone.model.Usuario;
import com.practica.carzone.repository.CitaRepository;
import com.practica.carzone.repository.ConcesionarioRepository;
import com.practica.carzone.repository.UsuarioRepository;

@Controller
@RequestMapping("/citas")
public class citaController {
    
    private Logger logger = LoggerFactory.getLogger(citaController.class);

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConcesionarioRepository concesionarioRepository;

    @GetMapping
    public String listaCitas(Model model) {

        List<Cita> listaCitas = citaRepository.findAll();

        model.addAttribute("citas", listaCitas);
        
        return "secundarias/cita/lista";
    }

    @GetMapping("/eliminar/{id}")
    public String removeCita(@PathVariable Long id, RedirectAttributes redAttrib) {
    
        if (!citaRepository.existsById(id))
            redAttrib.addFlashAttribute("error", "la cita no existe");
        else {
            citaRepository.deleteById(id);
            redAttrib.addFlashAttribute("success", "Se ha borrado correctamente la cita con el id " + id);
            logger.info("Se ha borrado correctamente la cita con el id " + id);
        }

        return "redirect:/citas";
    }

    @GetMapping("/nuevo")
    public String newCita(Model model) {

        Cita cita = new Cita();

        List<Usuario> listaUsuarios= usuarioRepository.findAll();

        List<Concesionario> listaConcesionarios = concesionarioRepository.findAll();

        model.addAttribute("cita", cita);

        model.addAttribute("usuarios", listaUsuarios);

        model.addAttribute("concesionarios", listaConcesionarios);

        return "secundarias/cita/nuevo";
    }

    @PostMapping("/crear")
    public String createCita(@ModelAttribute("cita") Cita cita) {
        
        citaRepository.save(cita);

        logger.info("Se ha creado la cita con el id " + cita.getId());

        return "redirect:/citas";
    }
    
    @GetMapping("/editar/{id}")
    public String editCita(@PathVariable Long id, Model model) {

        Cita cita = new Cita();

        List<Usuario> listaUsuarios= usuarioRepository.findAll();

        List<Concesionario> listaConcesionarios = concesionarioRepository.findAll();

        if (!citaRepository.existsById(id)) {
            model.addAttribute("error", "La cita no Existe");
        } else {
            cita = citaRepository.findById(id).get();
        }

        model.addAttribute("cita", cita);

        model.addAttribute("usuarios", listaUsuarios);

        model.addAttribute("concesionarios", listaConcesionarios);

        return "secundarias/cita/editar";
    }

    @PostMapping("/modificar")
    public String modifyCita(@ModelAttribute("cita") Cita cita, Model model) {

        try {
            citaRepository.save(cita); 
        } catch(Exception e) {
            model.addAttribute("error", "La cita no Existe");
        }

        return "redirect:/citas";
    }
    
}

