package com.colegio.gestion_asignaturas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.colegio.gestion_asignaturas.model.Asignatura;
import com.colegio.gestion_asignaturas.model.Usuario;
import com.colegio.gestion_asignaturas.repository.UsuarioRepository;
import com.colegio.gestion_asignaturas.service.AsignaturaService;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private final AsignaturaService asignaturaService;
    private final UsuarioRepository usuarioRepository;

    public DocenteController(AsignaturaService asignaturaService, UsuarioRepository usuarioRepository) {
        this.asignaturaService = asignaturaService;
        this.usuarioRepository = usuarioRepository;
    }

    //Muestra la lista de asignaturas ASIGNADAS a este DOCENTE.
    @GetMapping("/mis-asignaturas")
    public String listarMisAsignaturas(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        //Obtener el objeto Usuario de la sesión actual
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario Docente no encontrado."));

        //Validar que el usuario tenga un docente asociado
        if (usuario.getDocente() == null) {
            return "redirect:/logout"; 
        }

        //Usar el ID del docente para filtrar solo SUS asignaturas
        List<Asignatura> misAsignaturas = asignaturaService.findByDocenteId(usuario.getDocente().getId());
        model.addAttribute("asignaturas", misAsignaturas);

        return "docente/lista-docente"; // Nueva vista que crearemos
    }

    //Muestra el formulario para editar SOLO el horario.
    @GetMapping("/editar-horario/{id}")
    public String mostrarFormularioHorario(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Asignatura> optAsignatura = asignaturaService.findAsignaturaById(id);
        if (optAsignatura.isEmpty()) {
            return "redirect:/docente/mis-asignaturas";
        }

        Asignatura asignatura = optAsignatura.get();

        //Seguridad - Verificar que la asignatura realmente le pertenezca
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername()).get();

        if (usuario.getDocente() == null || 
            !asignatura.getDocenteEncargado().getId().equals(usuario.getDocente().getId())) {
            // Si la asignatura no es del docente logueado, le negamos el acceso (403 Forbidden)
            // Spring Security lo maneja, pero hacemos una doble verificación aquí.
            throw new org.springframework.security.access.AccessDeniedException("No tiene permiso para editar esta asignatura.");
        }

        model.addAttribute("asignatura", asignatura);
        return "docente/form-horario"; // Nueva vista que crearemos
    }

    //Procesa la actualización del horario.
    @PostMapping("/guardar-horario")
    public String guardarHorario(@ModelAttribute Asignatura formAsignatura, @AuthenticationPrincipal UserDetails userDetails) {

        //Obtener la asignatura ORIGINAL desde la BD
        Asignatura originalAsignatura = asignaturaService.findAsignaturaById(formAsignatura.getId())
            .orElseThrow(() -> new RuntimeException("Asignatura no encontrada."));

        //Verificar propiedad (Seguridad)
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername()).get();
        if (usuario.getDocente() == null || 
            !originalAsignatura.getDocenteEncargado().getId().equals(usuario.getDocente().getId())) {
            throw new org.springframework.security.access.AccessDeniedException("No tiene permiso para editar esta asignatura.");
        }

        //SOLO actualizamos los campos de horario y SALÓN (si es necesario)
        originalAsignatura.setHoraInicio(formAsignatura.getHoraInicio());
        originalAsignatura.setHoraFin(formAsignatura.getHoraFin());
        //Podrías añadir el salón si quieres permitirle al docente cambiarlo.

        asignaturaService.saveAsignatura(originalAsignatura);

        return "redirect:/docente/mis-asignaturas";
    }
}