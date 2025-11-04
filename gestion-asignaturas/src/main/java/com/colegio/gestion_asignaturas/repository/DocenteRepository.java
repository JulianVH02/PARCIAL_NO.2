package com.colegio.gestion_asignaturas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.gestion_asignaturas.model.Docente;

// JpaRepository toma dos parámetros:
// 1. La entidad (@Entity) que va a manejar (Docente)
// 2. El tipo de dato de la clave primaria (Long)
public interface DocenteRepository extends JpaRepository<Docente, Long> {

}