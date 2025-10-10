package com.capgroup.hotelmicroservices.msauthuser.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());

        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Requisição inválida",
                "message", "Erro de validação",
                "fieldErrors", errors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) status = HttpStatus.BAD_REQUEST;
        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", traduzirReason(status),
                "message", ex.getReason()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex) {
        var body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Conflito",
                "message", "Violação de integridade de dados"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
                var body = Map.of(
                                "timestamp", Instant.now().toString(),
                                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "error", "Erro interno do servidor",
                                "message", "Erro interno"
                );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, String> toFieldError(FieldError fe) {
        return Map.of(
                "field", fe.getField(),
                "message", fe.getDefaultMessage() == null ? "inválido" : fe.getDefaultMessage()
        );
    }

        private String traduzirReason(HttpStatus status) {
                return switch (status) {
                        case BAD_REQUEST -> "Requisição inválida";
                        case UNAUTHORIZED -> "Não autorizado";
                        case FORBIDDEN -> "Proibido";
                        case NOT_FOUND -> "Não encontrado";
                        case CONFLICT -> "Conflito";
                        case INTERNAL_SERVER_ERROR -> "Erro interno do servidor";
                        case SERVICE_UNAVAILABLE -> "Serviço indisponível";
                        default -> status.getReasonPhrase();
                };
        }
}
