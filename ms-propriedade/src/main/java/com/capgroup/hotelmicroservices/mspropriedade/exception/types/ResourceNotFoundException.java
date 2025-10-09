package com.capgroup.hotelmicroservices.mspropriedade.exception.types;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}