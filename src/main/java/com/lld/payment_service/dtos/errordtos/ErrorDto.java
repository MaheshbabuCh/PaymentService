package com.lld.payment_service.dtos.errordtos;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorDto {

    private String message;
    private HttpStatus errorCode;

}