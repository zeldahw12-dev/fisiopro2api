package com.fisio.fisio.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Cambia esto a false en PRODUCCIÓN para exigir JWT.
     * En dev lo dejamos true para no romper tu app actual mientras migras el login.
     */
    @Value("${app.security.permit-all:true}")
    private boolean permitAll;

    /** Secret para firmar/verificar JWT (HS256). Coloca un valor robusto por env/properties. */
    @Value("${app.jwt.secret:dev-secret-please-change-and-set-env}")
    private String jwtSecret;

    /** (Opcional) issuer a verificar si lo quieres usar; si vacío, no valida issuer. */
    @Value("${app.jwt.issuer:}")
    private String jwtIssuer;

    /** Rutas públicas (permitAll) siempre accesibles. Ajusta a tus necesidades. */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            // deja temporalmente abierto para login actual por email
            "/usuarios/email/**",
            // salud / docs opcional
            "/actuator/health",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Para almacenar/verificar contraseñas con hash seguro
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Config básica
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Autorización
        if (permitAll) {
            // Modo desarrollo: todo abierto (no rompe tu app actual)
            http.authorizeHttpRequests(reg -> reg
                    .requestMatchers("/**").permitAll()
                    .anyRequest().permitAll()
            );
        } else {
            // Modo producción: exige JWT en todo, excepto en lo público
            http.authorizeHttpRequests(reg -> reg
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated()
            );
            // Filtro de validación JWT (solo lectura del token)
            http.addFilterBefore(new JwtAuthFilter(jwtSecret, jwtIssuer), UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    /**
     * Filtro simple para validar Bearer JWT y poblar SecurityContext.
     * No crea endpoints ni servicios nuevos: solo confía en tokens existentes.
     * Espera un claim "roles" como lista de strings (p.ej. ["ROLE_ADMINISTRADOR"]).
     */
    static class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {
        private final SecretKey key;
        private final String issuer;

        JwtAuthFilter(String secret, String issuer) {
            this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            this.issuer = (issuer == null || issuer.isBlank()) ? null : issuer;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            try {
                Jws<Claims> jws = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);

                Claims claims = jws.getBody();

                if (issuer != null && !issuer.equals(claims.getIssuer())) {
                    throw new RuntimeException("Invalid issuer");
                }

                String subject = claims.getSubject(); // normalmente email o id
                if (!StringUtils.hasText(subject)) {
                    throw new RuntimeException("Missing subject");
                }

                // roles como claim opcional
                Object rolesClaim = claims.get("roles");
                Collection<SimpleGrantedAuthority> authorities = toAuthorities(rolesClaim);

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        subject, null, authorities
                );
                authentication.setDetails(request);

                // Establecer en el contexto
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                // Token inválido → sin autenticación
                org.springframework.security.core.context.SecurityContextHolder.clearContext();
            }

            filterChain.doFilter(request, response);
        }

        @SuppressWarnings("unchecked")
        private Collection<SimpleGrantedAuthority> toAuthorities(Object rolesClaim) {
            if (rolesClaim instanceof List<?> list) {
                return list.stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .filter(StringUtils::hasText)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
            return List.of();
        }
    }
}
