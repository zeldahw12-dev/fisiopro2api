package com.fisio.fisio.service;

import com.fisio.fisio.model.Usuario;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.price.premium.monthly}")
    private String priceMonthly;

    @Value("${stripe.price.premium.quarterly}")
    private String priceQuarterly;

    // Crea (o reutiliza) el Customer de Stripe
    public String ensureCustomer(Usuario usuario) throws StripeException {
        if (usuario.getStripeCustomerId() != null) {
            return usuario.getStripeCustomerId();
        }

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(usuario.getEmail())
                .setName(usuario.getNombre())
                .build();

        Customer customer = Customer.create(params);
        return customer.getId();
    }

    public Session createSubscriptionCheckoutSession(
            Usuario usuario,
            String planType,            // "MONTHLY" o "QUARTERLY"
            String successUrl,
            String cancelUrl
    ) throws StripeException {

        String customerId = ensureCustomer(usuario);
        String priceId = "MONTHLY".equalsIgnoreCase(planType)
                ? priceMonthly
                : priceQuarterly;

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setCustomer(customerId)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPrice(priceId)
                                        .build()
                        )
                        .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(cancelUrl)
                        .build();

        return Session.create(params);
    }
}
