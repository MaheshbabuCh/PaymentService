package com.lld.payment_service.paymentgateways;

public interface PaymentGateway {

   String generatePaymentLink(String orderId, long amount, String email) throws Exception;
}
