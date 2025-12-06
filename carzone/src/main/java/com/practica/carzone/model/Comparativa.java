package com.practica.carzone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data
@Entity
public class Comparativa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private LocalDate fechaCreacion;

    // Relacion con coche
    // mappedBy="comparativas" indica que el mapeo lo hace el atributo 'comparativas' en la clase Coche.
    @ManyToMany(mappedBy = "comparativas")
    private List<Coche> coches = new ArrayList<>();
}