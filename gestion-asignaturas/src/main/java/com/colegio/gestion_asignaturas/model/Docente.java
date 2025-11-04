package com.colegio.gestion_asignaturas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data // <-- Lombok para getters, setters, etc.
@Entity // <-- Le dice a JPA que esta clase es una tabla
@Table(name = "docentes") // <-- Nombre de la tabla en la BD
public class Docente {

    @Id // <-- Marca este campo como la Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- Autoincremental
    private Long id;
    
    private String nombreCompleto;
    
    // Podríamos añadir más campos como "email", "especialidad", etc.
    // pero por ahora lo mantenemos simple.
}