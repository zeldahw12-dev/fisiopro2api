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
     * ⚙️ Si es true → no se exige JWT (modo Render o desarrollo)
     * ⚙️ Si es false → exige JWT en endpoints no públicos
     */
    @Value("${app.security.permit-all:true}")
    private boolean permitAll;

    /** 🔐 Llave para firmar/verificar JWT */
    @Value("${app.jwt.secret:dev-secret-please-change-and-set-env}")
    private String jwtSecret;

    /** (Opcional) Issuer del token */
    @Value("${app.jwt.issuer:}")
    private String jwtIssuer;

    /** Endpoints siempre públicos */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/usuarios/email/**",
            "/actuator/health",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("[SecurityConfig] permitAll=" + permitAll);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (permitAll) {
            System.out.println("[SecurityConfig] All routes are open ✅");
            http.authorizeHttpRequests(reg -> reg.anyRequest().permitAll());
        } else {
            System.out.println("[SecurityConfig] JWT mode enabled 🔐");
            http.authorizeHttpRequests(reg -> reg
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated()
            );
            http.addFilterBefore(new JwtAuthFilter(jwtSecret, jwtIssuer),
                    UsernamePasswordAuthenticationFilter.class);
        }

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    /** 🧩 Filtro JWT: valida el Bearer token y autentica al usuario */
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
                                        FilterChain filterChain)
                throws ServletException, IOException {

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

                String subject = claims.getSubject();
                if (!StringUtils.hasText(subject)) {
                    throw new RuntimeException("Missing subject");
                }

                Object rolesClaim = claims.get("roles");
                Collection<SimpleGrantedAuthority> authorities = toAuthorities(rolesClaim);

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        subject, null, authorities
                );
                authentication.setDetails(request);

                org.springframework.security.core.context.SecurityContextHolder
                        .getContext().setAuthentication(authentication);

            } catch (Exception ex) {
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
