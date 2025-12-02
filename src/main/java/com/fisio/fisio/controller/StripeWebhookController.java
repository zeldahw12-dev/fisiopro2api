package com.fisio.fisio.controller;

import com.fisio.fisio.model.PlanTipo;
import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
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
        System.out.println("[StripeWebhook] payload recibido: " + payload);

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("[StripeWebhook] Firma inválida: " + e.getMessage());
            return ResponseEntity.status(400).body("Firma inválida");
        }

        String type = event.getType();
        System.out.println("[StripeWebhook] tipo=" + type);

        try {
            switch (type) {
                case "customer.subscription.created":
                case "customer.subscription.updated": {
                    Subscription sub = (Subscription) event.getDataObjectDeserializer()
                            .getObject().orElse(null);
                    if (sub != null) {
                        handleSubscriptionChange(sub);
                    }
                    break;
                }

                case "customer.subscription.deleted": {
                    Subscription sub = (Subscription) event.getDataObjectDeserializer()
                            .getObject().orElse(null);
                    if (sub != null) {
                        handleSubscriptionDeleted(sub);
                    }
                    break;
                }

                // Por si sólo estás recibiendo checkout.session.completed
                case "checkout.session.completed": {
                    Session session = (Session) event.getDataObjectDeserializer()
                            .getObject().orElse(null);
                    if (session != null) {
                        String subscriptionId = session.getSubscription();
                        System.out.println("[StripeWebhook] checkout.session.completed subscriptionId=" + subscriptionId);
                        if (subscriptionId != null) {
                            Subscription sub = Subscription.retrieve(subscriptionId);
                            handleSubscriptionChange(sub);
                        }
                    }
                    break;
                }

                default:
                    System.out.println("[StripeWebhook] Evento ignorado: " + type);
            }
        } catch (StripeException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error procesando webhook");
        }

        return ResponseEntity.ok("ok");
    }

    private void handleSubscriptionChange(Subscription sub) {
        String customerId = sub.getCustomer();
        String status = sub.getStatus(); // active, trialing, canceled, unpaid, etc.

        System.out.println("[StripeWebhook] subscription change customer=" + customerId + " status=" + status);

        Optional<Usuario> opt = usuarioRepository
                .findAll().stream()
                .filter(u -> customerId.equals(u.getStripeCustomerId()))
                .findFirst();

        if (opt.isEmpty()) {
            System.out.println("[StripeWebhook] No se encontró usuario con stripe_customer_id=" + customerId);
            return;
        }

        Usuario u = opt.get();
        u.setStripeSubscriptionId(sub.getId());
        u.setSubscriptionStatus(status);

        if ("active".equals(status) || "trialing".equals(status)) {
            u.setPlan(PlanTipo.PREMIUM);
        } else if ("canceled".equals(status) || "unpaid".equals(status)) {
            u.setPlan(PlanTipo.FREE);
        }

        usuarioRepository.save(u);
        System.out.println("[StripeWebhook] Usuario " + u.getIdUsuario() +
                " actualizado, plan=" + u.getPlan() +
                ", status=" + u.getSubscriptionStatus());
    }

    private void handleSubscriptionDeleted(Subscription sub) {
        String customerId = sub.getCustomer();
        System.out.println("[StripeWebhook] subscription deleted customer=" + customerId);

        Optional<Usuario> opt = usuarioRepository
                .findAll().stream()
                .filter(u -> customerId.equals(u.getStripeCustomerId()))
                .findFirst();

        if (opt.isEmpty()) {
            System.out.println("[StripeWebhook] No se encontró usuario con stripe_customer_id=" + customerId);
            return;
        }

        Usuario u = opt.get();
        u.setStripeSubscriptionId(null);
        u.setSubscriptionStatus("canceled");
        u.setPlan(PlanTipo.FREE);
        usuarioRepository.save(u);

        System.out.println("[StripeWebhook] Usuario " + u.getIdUsuario() +
                " pasado a FREE por subscription.deleted");
    }
}
