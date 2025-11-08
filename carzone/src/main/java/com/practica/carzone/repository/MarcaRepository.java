package com.practica.carzone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.practica.carzone.model.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    
    // Ya esta por ahora

    public List<Marca> findByNombreMarcaAndPaisOrigen(String nombreMarca, String pais);

    public Marca findTop1ByNombreMarcaOrderByPaisOrigenDesc(String nombreMarca);

    public int countByPaisOrigen(String pais);

    @Query(value = "SELECT m.nombreMarca FROM Marca m")
    List<String> mostrarMarcas();

    @Query(value = "SELECT m.nombreMarca FROM Marca m WHERE LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :nombreParam, '%'))")
    List<String> mostrarMarcasCaseSensitive(@Param("nombreParam") String nombreMarca);

}
