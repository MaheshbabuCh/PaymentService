package com.lld.payment_service.dtos.requestdtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

   private String orderId;
   private  String email;
}
