package com.colegio.gestion_asignaturas.controller; // O donde manejes tus controladores

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController {

    @GetMapping("/error-403")
    public String accessDenied() {
        return "error-403"; 
    }
}