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

    /**
     * Obtiene y muestra el listado de todas las comparativas
     * @param model modelo para pasar el listado de comparativas a la vista
     * @return vista del listado de comparativas
     */
    @GetMapping
    public String listaComparativas(Model model) {

        List<Comparativa> listaComparativas = comparativaRepository.findAll();

        model.addAttribute("comparativas", listaComparativas);
        
        return "secundarias/comparativa/lista";
    }

    /**
     * Elimina una comparativa del repositorio por su identificador
     * @param id identificador único de la comparativa a eliminar
     * @param redAttrib atributos para redirigir con mensajes flash
     * @return redirección al listado de comparativas
     */
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

    /**
     * manda al formulario para crear una nueva comparativa
     * @param model modelo para pasar la instancia de comparativa a la vista
     * @return vista del formulario de creación de nueva comparativa
     */
    @GetMapping("/nuevo")
    public String newComparativa(Model model) {

        Comparativa comparativa = new Comparativa();

        model.addAttribute("comparativa", comparativa);

        return "secundarias/comparativa/nuevo";
    }

    /**
     * Crea una nueva comparativa en el repositorio
     * @param comparativa objeto Comparativa con los datos a guardar
     * @return redirección al listado de comparativas
     */
    @PostMapping("/crear")
    public String createComparativa(@ModelAttribute("comparativa") Comparativa comparativa) {
        
        comparativaRepository.save(comparativa);

        logger.info("Se ha creado la comparativa con el id " + comparativa.getId());

        return "redirect:/comparativas";
    }
    
    /**
     * Manda al formulario de edición para una comparativa existente
     * @param id identificador único de la comparativa a editar
     * @param model modelo para pasar la comparativa a la vista
     * @return vista del formulario de edición de comparativa
     */
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

    /**
     * Modifica los datos de una comparativa existente en el repositorio
     * @param comparativa objeto Comparativa con los datos actualizados
     * @param model modelo para pasar mensajes de error a la vista si es necesario
     * @return redirección al listado de comparativas
     */
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