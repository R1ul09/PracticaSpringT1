package com.practica.carzone.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    

    /**
     * GET /api/marcas/busqueda/porNombre/caseSensitive?nombre={nombre}
     * Utiliza el método mostrarMarcasCaseSensitive (@Query)
     * @param nombre Nombre exacto de la marca a buscar
     * @return Lista de marcas coincidentes o 204 No Content
     */
    @GetMapping("/marcas/busqueda/porNombre/caseSensitive")
    public ResponseEntity<List<Marca>> buscarPorNombreCaseSensitive(@RequestParam("nombre") String nombre) {
                
        List<Marca> marcas = marcaRepository.mostrarMarcasCaseSensitive(nombre);
        
        if (marcas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(marcas, HttpStatus.OK);
    }
    
}
