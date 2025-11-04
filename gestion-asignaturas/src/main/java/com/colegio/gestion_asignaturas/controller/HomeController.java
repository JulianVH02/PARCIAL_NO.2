package com.colegio.gestion_asignaturas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Maneja la ruta de inicio (http://localhost:8080/)
    @GetMapping("/")
    public String home() {
        // Redirecciona al usuario autenticado a la lista de asignaturas
        return "redirect:/asignaturas/lista";
    }

    // Muestra el formulario de login. Spring Security ya está configurado
    // para buscar la vista 'login.html' en la ruta '/login'.
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Ruta a donde redirige el login exitoso si no hay una ruta específica solicitada
    @GetMapping("/home")
    public String dashboard() {
        // Redirecciona al usuario a la lista de asignaturas (nuestra página principal)
        return "redirect:/asignaturas/lista";
    }
}