package com.fisio.fisio.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // Configura la API key global de Stripe (modo prueba o producción según la clave)
        Stripe.apiKey = secretKey;
        System.out.println("[Stripe] API Key configurada (modo " +
                (secretKey.startsWith("sk_test_") ? "TEST" : "LIVE") + ")");
    }
}
