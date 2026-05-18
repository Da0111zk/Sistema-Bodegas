

package com.example.ms_stock.exception;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   
public class GlobalException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();// LinkedHashMap mantiene el orden de inserción.
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
    // ── ERROR DE NEGOCIO (categoría no encontrada, etc.) ──
    // Se dispara cuando el Service lanza RuntimeException,
    // por ejemplo: "Categoría no encontrada con id: 99"
 // 400 Bad Request: el cliente envió un dato que no existe
        // (un categoriaId inválido es un error del cliente, no del servidor).
        // Usamos 400 y no 500 porque el servidor funcionó correctamente;
        // fue el dato enviado el que causó el problema.
}
