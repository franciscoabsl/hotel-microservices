package com.capgroup.hotelmicroservices.msreserva.core.domain.exceptions;

public class RecursoNaoDisponivelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoDisponivelException(String message) {
        super(message);
    }
}
