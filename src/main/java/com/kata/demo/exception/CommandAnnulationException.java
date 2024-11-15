package com.kata.demo.exception;

import lombok.Getter;

@Getter
public class CommandAnnulationException extends RuntimeException{

    private String message;
    private final String businessCode = "RULE_001";

    public CommandAnnulationException(String message){
        super(message);
        this.message = message;
    }
}
