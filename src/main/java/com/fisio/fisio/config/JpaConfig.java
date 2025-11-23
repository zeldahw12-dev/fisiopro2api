package com.fisio.fisio.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Fuerza timezone UTC en JDBC/Hibernate para evitar desfases entre servidor y móvil.
 * (Requiere que guardes/recuperes en UTC en el cliente o conviertas en la app).
 */
@Configuration
public class JpaConfig {

    @Bean
    public HibernatePropertiesCustomizer forceUtcTimeZone() {
        return (Map<String, Object> props) -> props.put("hibernate.jdbc.time_zone", "UTC");
    }
}
