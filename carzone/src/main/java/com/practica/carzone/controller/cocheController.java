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

    @GetMapping("/busquedaAvanzada")
    public String busquedaAvanzada(
        @RequestParam(value = "color", required = false) String color,
        @RequestParam(value = "anio", required = false) String anioStr,
        Model model) {
        
        List<Coche> listaCoches = List.of();
        
        // Convertir el año a Year para la consulta si está presente
        if (color != null && !color.isEmpty() && anioStr != null && !anioStr.isEmpty()) {
            try {
                java.time.Year anio = java.time.Year.parse(anioStr);
                
                // 1. Ejecución del método de búsqueda avanzada del Repository
                listaCoches = cocheRepository.findByColorAndAnioLessThan(color, anio);
                
                logger.info("Búsqueda avanzada de coches ejecutada: Color={}, Año < {}", color, anioStr);

                if (listaCoches.isEmpty()) {
                    model.addAttribute("warning", "No se encontraron coches con el color " + color + " y año anterior a " + anioStr);
                }
            } catch (Exception e) {
                model.addAttribute("error", "Ocurrió un error inesperado al buscar.");
                logger.error("Error en la búsqueda avanzada de coches.", e);
            }
        }

        model.addAttribute("colorCoche", color);
        model.addAttribute("anioLimite", anioStr);
        model.addAttribute("listaCoches", listaCoches);

        // Ejecución de la consulta JPQL con JOIN (Coches caros de Alemania)
        // Usamos datos fijos para demostrar la funcionalidad JPQL
        int precioMinimo = 20000;
        String paisOrigen = "Alemania";
        
        List<Coche> cochesAlemanesCaros = cocheRepository.cochesMayorde20000ydeAlemania(precioMinimo, paisOrigen);
        
        model.addAttribute("precioMinimo", precioMinimo);
        model.addAttribute("paisOrigenFiltro", paisOrigen);
        model.addAttribute("cochesAlemanesCaros", cochesAlemanesCaros);

        return "principales/coche/busquedaAvanzada";
    }

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model model) {
        
        // Coche más caro (findTop1ByOrderByPrecioDesc)
        Coche cocheTopPrecio = cocheRepository.findTop1ByOrderByPrecioDesc();
        
        // Conteo por Modelo (countByModelo)
        String modeloEjemplo = "Corolla";
        int countModelo = cocheRepository.countByModelo(modeloEjemplo);

        model.addAttribute("cocheTopPrecio", cocheTopPrecio);
        model.addAttribute("modeloEjemplo", modeloEjemplo);
        model.addAttribute("countModelo", countModelo);
        
        logger.info("Estadísticas de coches cargadas. Coche más caro ID: {}", 
                    cocheTopPrecio != null ? cocheTopPrecio.getId() : "N/A");

        return "principales/coche/estadisticas";
    }

    @GetMapping("/borrarPorAnio")
    public String borrarPorAnio(@RequestParam(value = "anio", required = false) String anioStr, RedirectAttributes redAttrib) {
        
        if (anioStr == null || anioStr.isEmpty()) {
            redAttrib.addFlashAttribute("error", "Debe proporcionar un año");
            return "redirect:/coches";
        }

        try {
            java.time.Year anio = java.time.Year.parse(anioStr);
            
            // Ejecución del método @Transactional
            cocheRepository.deleteByAnioGreaterThan(anio); 
            
            redAttrib.addFlashAttribute("success", "Borrado transaccional ejecutado: Se eliminaron los coches con año superior a " + anioStr);
            logger.warn("SE HA EJECUTADO EL BORRADO TRANSACCIONAL DE COCHES CON AÑO > {}", anioStr);
            
        } catch (Exception e) {
            redAttrib.addFlashAttribute("error", "Error al ejecutar el borrado transaccional: " + e.getMessage());
            logger.error("Error al ejecutar deleteByAnioGreaterThan", e);
        }

        return "redirect:/coches";
    }

}
