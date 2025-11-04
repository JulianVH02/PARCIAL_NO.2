package com.colegio.gestion_asignaturas.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "asignaturas")
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Requerimiento: "alfanumérico que no debe superar los 30 caracteres"
    @Column(name = "nombre", length = 30, nullable = false)
    private String nombre;
    
    // Requerimiento: "No debe superar los 100 caracteres"
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    
    // Requerimiento: "Campo numérico". Lo ponemos como String
    // por flexibilidad (Ej: "101-A"), pero nos aseguraremos
    // en la vista que solo se metan números si es necesario.
    @Column(name = "salon", length = 10)
    private String salon;
    
    // Requerimiento: "Hora de inicio y hora de finalización"
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin")
    private LocalTime horaFin;
    
    // --- Relación con Docente ---
    // Requerimiento: "Campo de selección desplegable"
    
    // Muchos-a-Uno: Muchas asignaturas pueden tener UN docente.
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "docente_id", nullable = false) // <-- Clave foránea
    private Docente docenteEncargado;

}