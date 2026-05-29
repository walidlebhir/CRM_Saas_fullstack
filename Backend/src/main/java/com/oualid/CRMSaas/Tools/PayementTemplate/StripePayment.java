package com.oualid.CRMSaas.Tools.PayementTemplate;

import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StripePayment extends PaymentTemplate {

    private static final String STRIPE_API_KEY = System.getenv("STRIPE_API_KEY");

    public StripePayment(PaymentRepository repo) {
        super(repo);
        if (STRIPE_API_KEY != null && !STRIPE_API_KEY.isEmpty()) {
            Stripe.apiKey = STRIPE_API_KEY;
        }
    }

    @Override
    protected void validate(PayementRequest payment) {
        if (payment.amount() == null || payment.amount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }
        if (payment.cardNumber() == null || payment.cardNumber().replaceAll("\\s", "").length() < 13) {
            throw new RuntimeException("Invalid card number");
        }
        if (payment.cardHolder() == null || payment.cardHolder().isBlank()) {
            throw new RuntimeException("Cardholder name is required");
        }
        if (payment.expiryDate() == null || !isValidExpiry(payment.expiryDate())) {
            throw new RuntimeException("Invalid expiry date");
        }
        if (payment.cvv() == null || payment.cvv().length() < 3) {
            throw new RuntimeException("Invalid CVV");
        }
        if (!luhnCheck(payment.cardNumber().replaceAll("\\s", ""))) {
            throw new RuntimeException("Card number failed validation");
        }
    }

    @Override
    protected boolean Execute(PayementRequest datarequest) {
        System.out.println("Stripe processing charge of " + datarequest.amount() + " EUR...");

        if (STRIPE_API_KEY != null && !STRIPE_API_KEY.isEmpty()) {
            try {
                String cardNumberClean = datarequest.cardNumber().replaceAll("\\s", "");
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(Math.round(datarequest.amount() * 100))
                        .setCurrency("eur")
                        .setDescription("CRM SaaS - Monthly subscription")
                        .putMetadata("card_last_four", cardNumberClean.substring(cardNumberClean.length() - 4))
                        .putMetadata("card_holder", datarequest.cardHolder())
                        .build();

                PaymentIntent intent = PaymentIntent.create(params);
                System.out.println("Stripe charge succeeded: " + intent.getId());
                return true;
            } catch (StripeException e) {
                System.err.println("Stripe error: " + e.getMessage());
                return false;
            }
        }

        // Simulation mode (no API key configured)
        System.out.println("Stripe simulation: charge of " + datarequest.amount() + " EUR approved");
        return true;
    }

    private boolean isValidExpiry(String expiry) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth exp = YearMonth.parse(expiry.trim(), formatter);
            return !exp.isBefore(YearMonth.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';
            if (digit < 0 || digit > 9) return false;
            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }
}
