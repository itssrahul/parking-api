package com.tollparking.lib.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class BookingAlreadyExists extends Exception {

    public BookingAlreadyExists(String message) {
        super(message);
    }
}