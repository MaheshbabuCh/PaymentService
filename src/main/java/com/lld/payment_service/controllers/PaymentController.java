package com.lld.payment_service.controllers;

import com.lld.payment_service.services.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    StripePaymentService paymentService;

    @Autowired
    public PaymentController(StripePaymentService paymentService) {
        this.paymentService = paymentService;
    }



    @PostMapping("/generate-link/")
    public ResponseEntity<String> generatePaymentLink(@RequestBody String orderId) {
        // Logic to generate payment link

        // 008068905067

       String url =  paymentService.generatePaymentLink(orderId);

       return ResponseEntity.ok(url);

       // return "payment Link";
    }
}
