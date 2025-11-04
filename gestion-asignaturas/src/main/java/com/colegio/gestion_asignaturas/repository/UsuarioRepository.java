package com.colegio.gestion_asignaturas.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.gestion_asignaturas.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA crea esta consulta por ti: SELECT * FROM usuarios WHERE username = ?
    Optional<Usuario> findByUsername(String username);

}