package com.sensedia.sample.consents.handler;

import com.sensedia.sample.consents.exception.ConsentException;
import com.sensedia.sample.consents.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConsentException.class)
    public ResponseEntity<ErrorResponse> handlerConsentException(ConsentException e) {

        log.info("Consent error: {}", e.getMessage());

        ErrorResponse response = new ErrorResponse(e.getStatus().value(), e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerArgumentNotValidException(MethodArgumentNotValidException e) {

        log.info("Validation error: {}", e.getMessage());

        String message = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);

        return ResponseEntity.badRequest().body(response);
    }
}
