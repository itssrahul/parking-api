package com.tollparking.lib.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoParkingFound extends Exception {

    public NoParkingFound(String message) {
        super(message);
    }
}