package com.tollparking.lib.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidParkingSlot extends Exception {

    public InvalidParkingSlot(String message) {
        super(message);
    }
}