package com.tollparking.lib.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoParkingSlot extends Exception {

    public NoParkingSlot(String message) {
        super(message);
    }
}