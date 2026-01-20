package com.lld.payment_service.services;

import com.lld.payment_service.dtos.requestdtos.PaymentRequestDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface PaymentService {

    String generatePaymentLink(String orderId, long amount, String email) throws Exception;
}
