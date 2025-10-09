package com.capgroup.hotelmicroservices.mspropriedade.exception.model;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}