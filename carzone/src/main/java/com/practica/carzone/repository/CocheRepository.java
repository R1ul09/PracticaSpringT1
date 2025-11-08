package com.practica.carzone.repository;

import java.time.Year;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.practica.carzone.model.Coche;

import jakarta.transaction.Transactional;

public interface CocheRepository extends JpaRepository<Coche, Long> {
    
    // Ya esta por ahora

    @Transactional
    public void deleteByAnioGreaterThan(Year Anio);

    @Transactional
    public void deleteByColor(String color);

    public List<Coche> findByColorAndAnioLessThan(String color, Year anio);

    @Query(value = "SELECT c FROM Coche c " + "JOIN c.marca m WHERE c.precio >= :precio AND m.paisOrigen = :paisOrigen")
    public List<Coche> cochesMayorde20000ydeAlemania(@Param("precio") int precio, @Param("paisOrigen") String paisOrigen);


}
