package com.colegio.gestion_asignaturas.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.gestion_asignaturas.model.Asignatura;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    // Nuevo método: Encuentra todas las asignaturas donde el docente_id es igual al ID pasado
    List<Asignatura> findByDocenteEncargadoId(Long docenteId);
}