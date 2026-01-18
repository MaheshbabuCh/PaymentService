package com.lld.payment_service.services;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String generatePaymentLink(String orderId) {
        // Logic to generate payment link
        return "payment Link for order: " + orderId;
    }
}
