package com.fisio.fisio.config;

import io.github.bucket4j.Bucket;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
import org.springframework.http.HttpMethod;
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
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.security.permit-all:true}")
    private boolean permitAll;

    @Value("${app.jwt.secret:dev-secret-please-change-and-set-env}")
    private String jwtSecret;

    @Value("${app.jwt.issuer:}")
    private String jwtIssuer;

    @Value("${app.jwt.algorithm:HS256}")
    private String jwtAlgorithm;

    @Value("${app.jwt.public-key-base64:}")
    private String jwtPublicKeyBase64;

    @Value("${app.security.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${app.security.rate-limit.capacity:100}")
    private long rateLimitCapacity;

    @Value("${app.security.rate-limit.refill-per-seconds:60}")
    private long rateLimitRefillSeconds;

    // ⚠️ IMPORTANTE: aquí ya incluimos el webhook de Stripe
    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/usuarios/email/**",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/stripe/webhook"         // <--- webhook accesible sin JWT
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("[SecurityConfig] permitAll=" + permitAll);
        System.out.println("[SecurityConfig] jwtAlgorithm=" + jwtAlgorithm);
        System.out.println("[SecurityConfig] rateLimitEnabled=" + rateLimitEnabled);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 🚦 Rate limiting (antes que cualquier otra cosa)
        if (rateLimitEnabled) {
            http.addFilterBefore(
                    new RateLimitingFilter(rateLimitCapacity, rateLimitRefillSeconds),
                    UsernamePasswordAuthenticationFilter.class
            );
        }

        if (permitAll) {
            System.out.println("[SecurityConfig] All routes are open ✅ (modo desarrollo)");
            http.authorizeHttpRequests(reg -> reg
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().permitAll()
            );
        } else {
            System.out.println("[SecurityConfig] JWT mode enabled 🔐");

            http.authorizeHttpRequests(reg -> reg
                    // públicos generales
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    // por si acaso queremos ser más explícitos sólo para POST
                    .requestMatchers(HttpMethod.POST, "/stripe/webhook").permitAll()
                    .anyRequest().authenticated()
            );

            Key verificationKey = buildVerificationKey();
            http.addFilterBefore(
                    new JwtAuthFilter(verificationKey, jwtIssuer),
                    UsernamePasswordAuthenticationFilter.class
            );
        }

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    private Key buildVerificationKey() {
        try {
            if ("RS256".equalsIgnoreCase(jwtAlgorithm)) {
                if (!StringUtils.hasText(jwtPublicKeyBase64)) {
                    throw new IllegalStateException("app.jwt.algorithm=RS256 pero app.jwt.public-key-base64 está vacío");
                }
                byte[] decoded = Base64.getDecoder().decode(jwtPublicKeyBase64);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey publicKey = kf.generatePublic(spec);
                System.out.println("[SecurityConfig] Usando RSA pública para verificar JWT (RS256)");
                return publicKey;
            }

            if (!StringUtils.hasText(jwtSecret) || jwtSecret.length() < 32) {
                throw new IllegalStateException(
                        "app.jwt.secret es demasiado corto. Usa un secreto de al menos 32 caracteres en producción."
                );
            }
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            System.out.println("[SecurityConfig] Usando HMAC (HS256) para JWT");
            return key;

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error al construir la clave de verificación JWT", e);
        }
    }

    /** 🔐 Filtro JWT */
    static class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {
        private final Key key;
        private final String issuer;

        JwtAuthFilter(Key key, String issuer) {
            this.key = key;
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
                    throw new JwtException("Invalid issuer");
                }

                String subject = claims.getSubject();
                if (!StringUtils.hasText(subject)) {
                    throw new JwtException("Missing subject");
                }

                Object rolesClaim = claims.get("roles");
                Collection<SimpleGrantedAuthority> authorities = toAuthorities(rolesClaim);

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        subject, null, authorities
                );
                authentication.setDetails(request);

                org.springframework.security.core.context.SecurityContextHolder
                        .getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);

            } catch (ExpiredJwtException ex) {
                org.springframework.security.core.context.SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
            } catch (JwtException ex) {
                org.springframework.security.core.context.SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido");
            } catch (Exception ex) {
                org.springframework.security.core.context.SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Error al validar token");
            }
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

    /** 🚦 Rate limiting por IP usando Bucket4j (API nueva con Bucket.builder()) */
    static class RateLimitingFilter extends org.springframework.web.filter.OncePerRequestFilter {

        private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
        private final long capacity;
        private final long refillSeconds;

        RateLimitingFilter(long capacity, long refillSeconds) {
            this.capacity = capacity;
            this.refillSeconds = refillSeconds;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            String ip = resolveClientIp(request);
            Bucket bucket = cache.computeIfAbsent(ip, this::newBucket);

            if (bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(429);
                response.getWriter().write("Too Many Requests");
            }
        }

        private Bucket newBucket(String key) {
            return Bucket.builder()
                    .addLimit(limit -> limit
                            .capacity(capacity)
                            .refillIntervally(capacity, Duration.ofSeconds(refillSeconds)))
                    .build();
        }

        private String resolveClientIp(HttpServletRequest request) {
            String forwarded = request.getHeader("X-Forwarded-For");
            if (StringUtils.hasText(forwarded)) {
                return forwarded.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        }
    }
}
