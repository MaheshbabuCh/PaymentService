package com.lld.payment_service.controllers;

import com.lld.payment_service.dtos.requestdtos.PaymentRequestDto;
import com.lld.payment_service.dtos.responsedtos.PaymentResponseDto;
import com.lld.payment_service.exceptions.UnableToGenerateLink;
import com.lld.payment_service.paymentgateways.stripepaymentgateway.StripePaymentGateway;
import com.lld.payment_service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }



    @PostMapping("/generate-link/")
    public ResponseEntity<PaymentResponseDto> generatePaymentLink(@RequestBody PaymentRequestDto paymentRequestDto) throws UnableToGenerateLink {

        String orderId = paymentRequestDto.getOrderId();
        // For demonstration, using a fixed amount. In real scenarios, fetch the amount based on orderId
        // Order order = orderService.getOrderById(orderId);
        // long amount = order.getAmount();
        // Here, we will just use a placeholder amount
        long amount = 50000; // Example amount, this should ideally come from the request or order details
        String email = paymentRequestDto.getEmail();

        try{
            String url =  paymentService.generatePaymentLink(paymentRequestDto.getOrderId(), amount, email);
            PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
            paymentResponseDto.setUrl(url);
            paymentResponseDto.setStatus("Success");
            paymentResponseDto.setAmount(amount);

            return new ResponseEntity<>(paymentResponseDto, HttpStatus.OK);

        }catch(Exception ex){
            throw new UnableToGenerateLink(ex.getMessage());
        }



    }
}
