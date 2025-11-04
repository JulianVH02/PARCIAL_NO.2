package com.colegio.gestion_asignaturas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Usaremos 'username' como el identificador único para login
    @Column(unique = true, nullable = false, length = 50)
    private String username; 
    
    // Guardará la contraseña ya encriptada (luego veremos eso)
    // El largo 60 es estándar para el formato BCrypt
    @Column(nullable = false, length = 60) 
    private String password;
    
    // Un booleano para poder "desactivar" cuentas si es necesario
    private boolean enabled = true;
    
    // --- La parte clave: El Rol ---
    // Le decimos a JPA que guarde el nombre del Enum (Ej: "ROLE_DOCENTE")
    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private RolNombre rol;
    
    // --- Vínculo clave con Docente ---
    
    // El requisito dice: "DOCENTE... que SOLO tenga cargo".
    // Necesitamos saber qué usuario corresponde a qué docente.
    
    // Relación Uno-a-Uno: Un Usuario está vinculado a UN Docente.
    // Es 'nullable = true' porque el RECTOR o ESTUDIANTE
    // no necesitan ser un 'Docente' en la tabla 'docentes'.
    @OneToOne 
    @JoinColumn(name = "docente_id", nullable = true)
    private Docente docente;

}