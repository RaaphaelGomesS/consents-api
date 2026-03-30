package com.sensedia.sample.consents.exception;

public record ErrorResponse(Integer status,
                            String message) {
}
