package com.kata.demo.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {StockInsuffisantException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleStockInsuffisantException(StockInsuffisantException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getBusinessCode());
    }

    @ExceptionHandler(value = {CommandAnnulationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleCommandAnnulationException(CommandAnnulationException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), ex.getBusinessCode());
    }


}
