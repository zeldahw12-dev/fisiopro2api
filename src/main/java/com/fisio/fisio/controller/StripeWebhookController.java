package com.fisio.fisio.controller;

import com.fisio.fisio.model.PlanTipo;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/stripe")
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
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Firma inválida");
        }

        switch (event.getType()) {
            case "customer.subscription.created":
            case "customer.subscription.updated": {
                Subscription sub = (Subscription) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (sub != null) {
                    String customerId = sub.getCustomer();
                    String status     = sub.getStatus(); // active, canceled, etc.

                    // Buscar usuario por stripeCustomerId
                    Optional<Usuario> opt = usuarioRepository
                            .findAll().stream()
                            .filter(u -> customerId.equals(u.getStripeCustomerId()))
                            .findFirst();

                    if (opt.isPresent()) {
                        Usuario u = opt.get();
                        u.setStripeSubscriptionId(sub.getId());
                        u.setSubscriptionStatus(status);

                        if ("active".equals(status) || "trialing".equals(status)) {
                            u.setPlan(PlanTipo.PREMIUM);
                        } else if ("canceled".equals(status) || "unpaid".equals(status)) {
                            u.setPlan(PlanTipo.FREE);
                        }
                        usuarioRepository.save(u);
                    }
                }
                break;
            }

            case "customer.subscription.deleted": {
                Subscription sub = (Subscription) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (sub != null) {
                    String customerId = sub.getCustomer();
                    Optional<Usuario> opt = usuarioRepository
                            .findAll().stream()
                            .filter(u -> customerId.equals(u.getStripeCustomerId()))
                            .findFirst();
                    if (opt.isPresent()) {
                        Usuario u = opt.get();
                        u.setStripeSubscriptionId(null);
                        u.setSubscriptionStatus("canceled");
                        u.setPlan(PlanTipo.FREE);
                        usuarioRepository.save(u);
                    }
                }
                break;
            }
        }

        return ResponseEntity.ok("ok");
    }
}
