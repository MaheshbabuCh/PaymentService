package com.lld.payment_service.services;

import com.lld.payment_service.paymentgateways.PaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImplementation implements PaymentService {

    PaymentGateway paymentGateway;

    @Autowired
    public PaymentServiceImplementation(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    @Override
    public String generatePaymentLink(String orderId, long amount, String email) throws Exception {

        try{

            return paymentGateway.generatePaymentLink(orderId, amount, email);

        }catch(Exception e){
            throw new Exception("Error generating payment link: " + e.getMessage());
        }
    }
}
