package com.practica.carzone.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.practica.carzone.model.Marca;
import com.practica.carzone.repository.MarcaRepository;

import jakarta.servlet.http.HttpServletResponse;

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

        if (!marcaRepository.existsById(id))
            model.addAttribute("error", "La marca no Existe");
        else
            marca = marcaRepository.findById(id).get();

        model.addAttribute("marca", marca);

        return "principales/marca/detalle";
    }

    @GetMapping("/busquedaAvanzada")
    public String busquedaAvanzada(
        @RequestParam(value = "nombre", required = false) String nombreMarca,
        @RequestParam(value = "pais", required = false) String paisOrigen, Model model) {
            
            List<Marca> listaMarcas = List.of();

            if (nombreMarca != null && !nombreMarca.isEmpty() && paisOrigen != null && !paisOrigen.isEmpty()) {
                try {
                    listaMarcas = marcaRepository.findByNombreMarcaAndPaisOrigen(nombreMarca, paisOrigen);
                    logger.info("Búsqueda avanzada ejecutada por nombre: {} y país: {}", nombreMarca, paisOrigen);

                    if (listaMarcas.isEmpty()) {
                        model.addAttribute("error", "No se encontraron marcas con esos requisitos");
                    }
                } catch (Exception e) {
                    model.addAttribute("error", "ocurrio un error");
                    logger.error("Error en la busqueda avanzada", e.getMessage());
                }
            }

            model.addAttribute("nombreMarca", nombreMarca);
            model.addAttribute("paisOrigen", paisOrigen);
            model.addAttribute("listaMarcas", listaMarcas);

            return "principales/marca/busquedaAvanzada";
        }

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model model) {
        
        // contar marcas por un pais (Usando countByPaisOrigen)
        String paisEjemplo = "Alemania";
        int countAlemania = marcaRepository.countByPaisOrigen(paisEjemplo);

        // encontrar el Top 1 (Usando findTop1ByNombreMarcaOrderByPaisOrigenDesc)
        String nombreEjemplo = "Toyota";
        Marca topMarca = marcaRepository.findTop1ByNombreMarcaOrderByPaisOrigenDesc(nombreEjemplo);

        List<String> nombres = marcaRepository.mostrarMarcas();

        model.addAttribute("paisEjemplo", paisEjemplo);
        model.addAttribute("countAlemania", countAlemania);
        model.addAttribute("nombreEjemplo", nombreEjemplo);
        model.addAttribute("topMarca", topMarca);
        model.addAttribute("nombresMarcas", nombres);
        
        logger.info("Estadísticas cargadas: {} marcas de {}", countAlemania, paisEjemplo);

        return "principales/marca/estadisticas";
    }
    
    @GetMapping("/exportarCSV")
    public void exportCSV(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"marcas_listado.csv\"");

        try {
            List<Marca> listaMarcas = marcaRepository.findAll();

            PrintWriter writer = response.getWriter();

            writer.println("ID,Nombre,Pais_Origen,Fecha_Fundacion,Sede_Central,Modelos_Activos");

            for (Marca marca : listaMarcas) {
                writer.println(marca.getId() + "," + marca.getNombreMarca() + "," + marca.getPaisOrigen() + "," 
                + marca.getFechaFundacion() + "," + marca.getSedeCentral() + "," + marca.getNumModelosActivos());
            }

            writer.flush();
        } catch (IOException e) {
            logger.error("Error al escribir el CSV: " + e.getMessage());
        }
    }

    @GetMapping("/exportarPDF")
    public void exportPDF(HttpServletResponse response) {

        // 2. Configurar la respuesta HTTP para PDF
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"marcas_listado.pdf\"");

        try {
            List<Marca> listaMarcas = marcaRepository.findAll();

            Document document = new Document(PageSize.A4);

            // Conectar el Documento al flujo de salida de la respuesta
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            document.add(new Paragraph("Listado de Marcas de Coches", 
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(5);

            PdfPCell headerCell;
            headerCell = new PdfPCell(new Phrase("Id"));
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Nombre"));
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Pais_Origen"));
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Fecha_Fundacion"));
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Sede_Central"));
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Modelos_Activos"));
            table.addCell(headerCell);

            for (Marca marca : listaMarcas) {

                table.addCell(String.valueOf(marca.getId()));

                table.addCell(marca.getNombreMarca());

                table.addCell(marca.getPaisOrigen());

                table.addCell(String.valueOf(marca.getFechaFundacion()));

                table.addCell(marca.getSedeCentral());

                table.addCell(String.valueOf(marca.getNumModelosActivos()));
            }
            
            document.add(table);

            document.close();
            
        } catch (IOException e) {
            logger.error("Error al generar el PDF: " + e.getMessage());
        }

    }

}
