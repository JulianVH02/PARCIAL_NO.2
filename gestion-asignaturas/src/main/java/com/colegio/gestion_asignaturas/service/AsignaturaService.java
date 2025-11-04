package com.colegio.gestion_asignaturas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.colegio.gestion_asignaturas.model.Asignatura;
import com.colegio.gestion_asignaturas.model.Docente;
import com.colegio.gestion_asignaturas.repository.AsignaturaRepository;
import com.colegio.gestion_asignaturas.repository.DocenteRepository;

@Service
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;
    private final DocenteRepository docenteRepository;
    
    // Inyección de dependencias
    public AsignaturaService(AsignaturaRepository asignaturaRepository, DocenteRepository docenteRepository) {
        this.asignaturaRepository = asignaturaRepository;
        this.docenteRepository = docenteRepository;
    }
    
 // ... dentro de AsignaturaService.java

 // Método nuevo para filtrar por docente
 public List<Asignatura> findByDocenteId(Long docenteId) {
     return asignaturaRepository.findByDocenteEncargadoId(docenteId);
 }

    // --- Métodos CRUD de Asignatura ---

    public List<Asignatura> findAllAsignaturas() {
        return asignaturaRepository.findAll();
    }

    public Optional<Asignatura> findAsignaturaById(Long id) {
        return asignaturaRepository.findById(id);
    }
    
    public Asignatura saveAsignatura(Asignatura asignatura) {
        return asignaturaRepository.save(asignatura);
    }

    public void deleteAsignatura(Long id) {
        asignaturaRepository.deleteById(id);
    }

    // --- Métodos Auxiliares para el formulario ---
    
    // Necesitamos una lista de docentes para el campo de selección desplegable
    public List<Docente> findAllDocentes() {
        return docenteRepository.findAll();
    }
}