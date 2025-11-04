package com.colegio.gestion_asignaturas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.colegio.gestion_asignaturas.model.Docente;
import com.colegio.gestion_asignaturas.model.RolNombre;
import com.colegio.gestion_asignaturas.model.Usuario;
import com.colegio.gestion_asignaturas.repository.DocenteRepository;
import com.colegio.gestion_asignaturas.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    // Inyectamos los repositorios y el codificador de contraseñas
    private final DocenteRepository docenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(DocenteRepository docenteRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.docenteRepository = docenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            
            // Si el RECTOR no existe, creamos TODOS los datos iniciales.
            // Esto previene errores de duplicidad cuando ddl-auto=update.
            if (usuarioRepository.findByUsername("rector").isEmpty()) {
                
                // --- 1. Crear Entidades Docentes (para que tengan IDs) ---
                
                // Docente 1 (Profesor Álgebra)
                Docente doc1 = new Docente();
                doc1.setNombreCompleto("Profesor Álgebra");
                doc1 = docenteRepository.save(doc1); 

                // Docente 2 (Profesora Historia)
                Docente doc2 = new Docente();
                doc2.setNombreCompleto("Profesora Historia");
                doc2 = docenteRepository.save(doc2);
                
                // Docente 3 (Profesor Cálculo)
                Docente doc3 = new Docente();
                doc3.setNombreCompleto("Profesor Cálculo");
                doc3 = docenteRepository.save(doc3); 

                // --- 2. Crear Entidades Usuarios (Respetando el orden de roles) ---
                
                // 1. Usuario RECTOR (sin docente asociado)
                Usuario rector = new Usuario();
                rector.setUsername("rector");
                rector.setPassword(passwordEncoder.encode("password")); 
                rector.setRol(RolNombre.ROLE_RECTOR);
                usuarioRepository.save(rector);

                // 2. Usuario DOCENTE 1 (Asociado a doc1: Profesor Álgebra)
                Usuario userDoc1 = new Usuario();
                userDoc1.setUsername("docente1");
                userDoc1.setPassword(passwordEncoder.encode("password")); 
                userDoc1.setRol(RolNombre.ROLE_DOCENTE);
                userDoc1.setDocente(doc1); 
                usuarioRepository.save(userDoc1);
                
                // 3. Usuario DOCENTE 3 (Asociado a doc2: Profesora Historia)
                Usuario userDoc3 = new Usuario();
                userDoc3.setUsername("docente3"); // Nuevo nombre de usuario
                userDoc3.setPassword(passwordEncoder.encode("password")); 
                userDoc3.setRol(RolNombre.ROLE_DOCENTE);
                userDoc3.setDocente(doc2); // Asociamos al Docente 2
                usuarioRepository.save(userDoc3);
                
                // 4. Usuario DOCENTE 2 (Asociado a doc3: Profesor Cálculo)
                Usuario userDoc2 = new Usuario();
                userDoc2.setUsername("docente2");
                userDoc2.setPassword(passwordEncoder.encode("password")); 
                userDoc2.setRol(RolNombre.ROLE_DOCENTE);
                userDoc2.setDocente(doc3); // Asociación al Docente 3
                usuarioRepository.save(userDoc2);
                
                // 5. Usuario ESTUDIANTE
                Usuario estudiante = new Usuario();
                estudiante.setUsername("estudiante");
                estudiante.setPassword(passwordEncoder.encode("password")); 
                estudiante.setRol(RolNombre.ROLE_ESTUDIANTE);
                usuarioRepository.save(estudiante);

                System.out.println("--- Datos iniciales (Rector, 3 Docentes, Estudiante) cargados con éxito ---");
            } else {
                System.out.println("--- Base de datos poblada, omitiendo DataLoader ---");
            }
        };
    }
}