package com.tollparking.lib.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInput extends Exception{
    public InvalidInput(String message) {
        super(message);
    }
}
