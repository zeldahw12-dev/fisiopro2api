package com.fisio.fisio.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisio.fisio.model.PlanTipo;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    public StripeWebhookController(UsuarioRepository usuarioRepository,
                                   ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/webhook")
    @Transactional
    public ResponseEntity<String> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload
    ) {
        log.info("[StripeWebhook] llamada recibida");

        if (endpointSecret == null || endpointSecret.isBlank()) {
            log.error("[StripeWebhook] endpointSecret vacío. Revisa STRIPE_WEBHOOK_SECRET");
            return ResponseEntity.status(500).body("Webhook secret not configured");
        }

        Event event;
        try {
            // ✅ Verificamos firma
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.warn("[StripeWebhook] Firma inválida: {}", e.getMessage());
            return ResponseEntity.status(400).body("Firma inválida");
        }

        log.info("[StripeWebhook] event.type = {}", event.getType());

        switch (event.getType()) {
            case "customer.subscription.created":
            case "customer.subscription.updated":
            case "customer.subscription.deleted":
                handleSubscriptionEvent(event, payload);
                break;
            default:
                log.info("[StripeWebhook] Tipo de evento ignorado: {}", event.getType());
        }

        return ResponseEntity.ok("ok");
    }

    private void handleSubscriptionEvent(Event event, String payload) {
        try {
            // 👇 Parseamos el JSON crudo del webhook con Jackson
            JsonNode root = objectMapper.readTree(payload);
            JsonNode obj = root.path("data").path("object");

            String customerId = obj.path("customer").asText(null);
            String status = obj.path("status").asText(null);
            String subscriptionId = obj.path("id").asText(null);

            log.info("[StripeWebhook] subscription raw: type={} customerId={} status={} subId={}",
                    event.getType(), customerId, status, subscriptionId);

            if (customerId == null || customerId.isBlank()) {
                log.warn("[StripeWebhook] subscription sin customerId, abortando");
                return;
            }

            // 🔍 Buscar usuario por stripeCustomerId
            Optional<Usuario> opt = usuarioRepository.findByStripeCustomerId(customerId);
            if (opt.isEmpty()) {
                log.warn("[StripeWebhook] No encontré usuario con stripeCustomerId={}", customerId);
                return;
            }

            Usuario u = opt.get();
            u.setStripeSubscriptionId(subscriptionId);
            u.setSubscriptionStatus(status);

            if ("active".equals(status) || "trialing".equals(status)) {
                u.setPlan(PlanTipo.PREMIUM);
            } else if ("canceled".equals(status)
                    || "unpaid".equals(status)
                    || "incomplete_expired".equals(status)) {
                u.setPlan(PlanTipo.FREE);
            }

            usuarioRepository.save(u);
            log.info("[StripeWebhook] Usuario {} actualizado. plan={}, subscriptionStatus={}",
                    u.getIdUsuario(), u.getPlan(), u.getSubscriptionStatus());

        } catch (IOException e) {
            log.error("[StripeWebhook] Error parseando payload JSON", e);
        }
    }
}
