package com.practica.carzone.model;

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
public class Servicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreServicio;
    private String descripcion;
    private Double precioBase;

    // RELACIÓN MANY-TO-MANY CON CITA (Lado Inverso)
    // mappedBy="servicios" indica que el mapeo lo hace el atributo 'servicios' en la clase Cita.
    @ManyToMany(mappedBy = "servicios")
    private List<Cita> citas = new ArrayList<>();
}