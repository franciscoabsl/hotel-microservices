package com.capgroup.hotelmicroservices.msreserva.core.exceptions;

public class AcessoNegadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AcessoNegadoException(String message) {
        super(message);
    }
}
