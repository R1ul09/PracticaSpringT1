package com.practica.carzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practica.carzone.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    // Ya esta
    java.util.Optional<Usuario> findByEmail(String email);
    
}
