package com.lld.payment_service.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {

    private String url;
    private String status;
    private double amount;
}
