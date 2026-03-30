package com.sensedia.sample.consents.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ConsentException extends RuntimeException {

    private final HttpStatus status;

    public ConsentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
