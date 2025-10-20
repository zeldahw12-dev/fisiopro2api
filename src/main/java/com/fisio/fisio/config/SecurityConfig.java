package com.fisio.fisio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilita CSRF para APIs REST
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acceso sin autenticación a los endpoints de usuario y login
                        .requestMatchers("/usuarios/**").permitAll()
                        // Permite acceso sin autenticación a todos los endpoints para desarrollo
                        // En producción, deberías restringir esto y requerir autenticación para la mayoría
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
                );
        return http.build();
    }
}
