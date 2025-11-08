package com.practica.carzone.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.carzone.model.Coche;
import com.practica.carzone.model.Marca;

import com.practica.carzone.repository.CocheRepository;
import com.practica.carzone.repository.MarcaRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CocheRepository cocheRepository;

    @GetMapping("/marcas")
    public List<Marca> getAllMarcas() {

        return marcaRepository.findAll();
        
    }

    @GetMapping("/coches")
    public List<Coche> getAllCoches() {

        return cocheRepository.findAll();
        
    }

    @GetMapping("/marca/NombreyPais")
    public List<Marca> getMarcaNombreYPais(@RequestParam String nombreMarca, @RequestParam String pais) {
        
        return marcaRepository.findByNombreMarcaAndPaisOrigen(nombreMarca, pais);
        
    }
    
    
    
}
