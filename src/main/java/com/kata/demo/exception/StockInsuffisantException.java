package com.kata.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockInsuffisantException extends RuntimeException{

    private String message;
    private final String businessCode = "RULE_002";

    public StockInsuffisantException(String message){
        super(message);
        this.message = message;
    }
}
