package com.example.kardex.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Recurso o página no encontrada");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
        RuntimeException ex,
        HttpServletRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    String error = "Error de negocio";

    if (ex.getMessage() != null &&
        (ex.getMessage().contains("no encontrado")
        || ex.getMessage().contains("No existe stock")
        || ex.getMessage().contains("No existe configuración"))) {
        status = HttpStatus.NOT_FOUND;
        error = "Configuración de stock no encontrada";
    }

    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", status.value());
    body.put("error", error);
    body.put("message", ex.getMessage());
    body.put("path", request.getRequestURI());

    return ResponseEntity.status(status).body(body);
}
}