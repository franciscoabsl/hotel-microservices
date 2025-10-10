package com.capgroup.hotelmicroservices.msreserva.core.exceptions;

public class RecursoNaoDisponivelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoDisponivelException(String message) {
        super(message);
    }
}
