package com.colegio.gestion_asignaturas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.colegio.gestion_asignaturas.model.Asignatura;
import com.colegio.gestion_asignaturas.model.Docente;
import com.colegio.gestion_asignaturas.service.AsignaturaService;

@Controller
@RequestMapping("/asignaturas")
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    public AsignaturaController(AsignaturaService asignaturaService) {
        this.asignaturaService = asignaturaService;
    }

    @GetMapping("/lista")
    public String listarAsignaturas(Model model) {
        List<Asignatura> asignaturas = asignaturaService.findAllAsignaturas();
        model.addAttribute("asignaturas", asignaturas);
        return "lista-asignaturas"; // Retorna la vista lista-asignaturas.html
    }

    // --- B. Formulario de Creación/Edición (CREATE/UPDATE - SOLO RECTOR) ---
    // Regla de seguridad: hasRole('RECTOR')
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("asignatura", new Asignatura()); // Asignatura vacía para crear
        List<Docente> docentes = asignaturaService.findAllDocentes();
        model.addAttribute("docentes", docentes); // Lista para el desplegable
        return "form-asignatura";
    }

    // Para editar (el mismo formulario, pero cargando datos)
    // Regla de seguridad: hasRole('RECTOR')
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Asignatura> asignatura = asignaturaService.findAsignaturaById(id);
        if (asignatura.isPresent()) {
            model.addAttribute("asignatura", asignatura.get());
            model.addAttribute("docentes", asignaturaService.findAllDocentes());
            return "form-asignatura";
        }
        return "redirect:/asignaturas/lista"; // Si no existe, vuelve a la lista
    }

    // --- C. Guardar Asignatura (CREATE/UPDATE - SOLO RECTOR) ---
    // Regla de seguridad: hasRole('RECTOR')
    @PostMapping("/guardar")
    public String guardarAsignatura(@ModelAttribute Asignatura asignatura) {
        asignaturaService.saveAsignatura(asignatura);
        return "redirect:/asignaturas/lista";
    }

    // --- D. Eliminar Asignatura (DELETE - SOLO RECTOR) ---
    // Regla de seguridad: hasRole('RECTOR')
    @GetMapping("/eliminar/{id}")
    public String eliminarAsignatura(@PathVariable Long id) {
        // Antes de eliminar, podrías verificar si el usuario tiene permiso (aunque ya lo hicimos con Spring Security)
        asignaturaService.deleteAsignatura(id);
        return "redirect:/asignaturas/lista";
    }
    
    // NOTA: La funcionalidad de DOCENTE (actualizar solo horario) se manejará
    // con un controlador/vista aparte, para simplificar el código de este.
}