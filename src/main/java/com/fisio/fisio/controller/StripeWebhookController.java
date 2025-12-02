package com.fisio.fisio.controller;

import com.fisio.fisio.model.PlanTipo;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/stripe")
@Slf4j
public class StripeWebhookController {

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    private final UsuarioRepository usuarioRepository;

    public StripeWebhookController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
                handleSubscriptionEvent(event);
                break;
            default:
                log.info("[StripeWebhook] Tipo de evento ignorado: {}", event.getType());
        }

        return ResponseEntity.ok("ok");
    }

    private void handleSubscriptionEvent(Event event) {
        var deserializer = event.getDataObjectDeserializer();
        Subscription sub = (Subscription) deserializer.getObject().orElse(null);

        if (sub == null) {
            log.warn("[StripeWebhook] subscription event sin objeto (id={})", event.getId());
            return;
        }

        String customerId = sub.getCustomer();
        String status = sub.getStatus();
        String subscriptionId = sub.getId();

        log.info("[StripeWebhook] subscription: type={} customerId={} status={} subId={}",
                event.getType(), customerId, status, subscriptionId);

        if (customerId == null || customerId.isBlank()) {
            log.warn("[StripeWebhook] subscription sin customerId");
            return;
        }

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
    }
}
