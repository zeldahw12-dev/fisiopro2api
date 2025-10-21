package com.fisio.fisio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS centralizado. Controla orígenes por propiedad:
 *   app.cors.allowed-origins=https://tu-dominio.com,exp://192.168.0.10:19000,http://localhost:8081
 *
 * Si se deja vacío o "*", permite todo (útil en desarrollo).
 */
@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOriginsProp;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        List<String> origins;
        if (!StringUtils.hasText(allowedOriginsProp) || "*".equals(allowedOriginsProp.trim())) {
            cfg.addAllowedOriginPattern("*"); // comodín para dev
        } else {
            origins = Arrays.stream(allowedOriginsProp.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .toList();
            origins.forEach(cfg::addAllowedOrigin);
        }

        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.addAllowedHeader("*");
        cfg.setAllowCredentials(false); // ponlo en true solo si necesitas cookies
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
