package com.practica.carzone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List; 

import jakarta.persistence.CascadeType; 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Marca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombreMarca;
    private String paisOrigen;
    private LocalDate fechaFundacion;
    private String sedeCentral;
    private int numModelosActivos;

    // Relacion con coche
    // mappedBy="marca" indica que la columna de clave foránea está en la entidad Coche
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coche> coches = new ArrayList<>();
}