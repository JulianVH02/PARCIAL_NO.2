package com.colegio.gestion_asignaturas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importar BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    public AuthenticationProvider authenticationProvider(
        UserDetailsServiceImpl userDetailsService, // Spring inyecta aquí
        PasswordEncoder passwordEncoder) { // Spring inyecta aquí
        
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/login", "/css/**", "/js/**", "/error-403").permitAll() 
                
                .requestMatchers("/asignaturas/nueva", "/asignaturas/guardar", "/asignaturas/eliminar/**", "/asignaturas/editar/**").hasRole("RECTOR")
                .requestMatchers("/docente/**").hasRole("DOCENTE")
                .requestMatchers("/", "/asignaturas/lista", "/home").hasAnyRole("RECTOR", "DOCENTE", "ESTUDIANTE")
                
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml"
                ).permitAll()
                
                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/asignaturas/lista", true) 
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/error-403") 
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // Asumiendo que CacheClearingLogoutHandler está definido y en el paquete correcto
                .addLogoutHandler(new CacheClearingLogoutHandler()) 
                .logoutSuccessUrl("/login?logout") 
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}