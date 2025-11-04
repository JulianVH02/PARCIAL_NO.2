package com.colegio.gestion_asignaturas.security;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.colegio.gestion_asignaturas.model.Usuario;
import com.colegio.gestion_asignaturas.repository.UsuarioRepository;

@Service // Marca esta clase como un Service (Componente de lógica de negocio)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    //Inyección de dependencias del repositorio
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Este es el método que Spring Security llama durante el login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        //Busca el usuario en nuestra BD usando el método que definimos en el Repository
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        //Transforma nuestro RolNombre (Ej: ROLE_RECTOR) a una GrantedAuthority
        GrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().name());
        
        //Retorna un objeto UserDetails (propio de Spring Security)
        return new User(
                usuario.getUsername(),
                usuario.getPassword(),
                Collections.singletonList(authority) //Solo tiene un rol, lo convertimos a una lista
        );
    }
}