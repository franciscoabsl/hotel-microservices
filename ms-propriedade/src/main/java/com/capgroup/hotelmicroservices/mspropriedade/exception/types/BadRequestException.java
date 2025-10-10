package com.capgroup.hotelmicroservices.mspropriedade.exception.types;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
