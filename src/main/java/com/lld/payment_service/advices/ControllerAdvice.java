package com.lld.payment_service.advices;

import com.lld.payment_service.dtos.errordtos.ErrorDto;
import com.lld.payment_service.exceptions.UnableToGenerateLink;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(UnableToGenerateLink.class)
    private ResponseEntity<ErrorDto> unableToGenerateLink(Exception ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        errorDto.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
