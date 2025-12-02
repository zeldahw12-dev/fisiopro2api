package com.fisio.fisio.controller;

import com.fisio.fisio.model.Usuario;
import com.fisio.fisio.repository.UsuarioRepository;
import com.fisio.fisio.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final StripeService stripeService;
    private final UsuarioRepository usuarioRepository;

    public PaymentController(StripeService stripeService,
                             UsuarioRepository usuarioRepository) {
        this.stripeService = stripeService;
        this.usuarioRepository = usuarioRepository;
    }

    public static class CreateCheckoutBody {
        public String plan; // "MONTHLY" o "QUARTERLY"
    }

    @PostMapping("/checkout-session")
    public ResponseEntity<?> createCheckoutSession(
            @RequestBody CreateCheckoutBody body,
            Authentication auth
    ) {
        try {
            Integer idUsuario = Integer.valueOf(auth.getName());
            Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
            if (opt.isEmpty()) {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }
            Usuario usuario = opt.get();

            String successUrl = "https://fisiopro.app/pago-exitoso";
            String cancelUrl  = "https://fisiopro.app/pago-cancelado";

            Session session = stripeService.createSubscriptionCheckoutSession(
                    usuario,
                    body.plan,
                    successUrl,
                    cancelUrl
            );

            return ResponseEntity.ok(Map.of(
                    "url", session.getUrl()
            ));

        } catch (StripeException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Error creando sesión de pago: " + ex.getMessage());
        }
    }
}
