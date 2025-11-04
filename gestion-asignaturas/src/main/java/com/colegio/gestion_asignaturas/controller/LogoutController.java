package com.colegio.gestion_asignaturas.controller; 
// Asegúrate de que el paquete sea el correcto

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout-final")
    public String finalLogout() {
        return "redirect:/login?logout";
    }
}