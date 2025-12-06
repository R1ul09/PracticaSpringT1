package com.practica.carzone.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private Year anio;
    private double precio;
    private String color;
    private String tipoCombustible;
    
    // Relacion con marca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", nullable = true)
    private Marca marca;

    // Relacion con concesionario
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "comparativa_coche",
        joinColumns = @JoinColumn(name = "coche_id"),
        inverseJoinColumns = @JoinColumn(name = "comparativa_id")
    )
    private List<Comparativa> comparativas = new ArrayList<>();
}