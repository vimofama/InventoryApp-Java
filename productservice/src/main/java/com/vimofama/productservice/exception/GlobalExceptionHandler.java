package com.vimofama.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Los datos enviados no son válidos");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();

        String message = "Formato de datos inválido";
        String detailedMessage = ex.getMessage();

        // Intentar extraer información más específica del error
        if (detailedMessage != null) {
            if (detailedMessage.contains("BigDecimal")) {
                message = "El formato del precio no es válido. Use punto (.) como separador decimal (ejemplo: 19.99)";
            } else if (detailedMessage.contains("Cannot deserialize")) {
                message = "Uno o más campos tienen un formato incorrecto";
            } else if (detailedMessage.contains("JSON parse error")) {
                message = "Error en el formato JSON enviado";
            }
        }

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", message);
        response.put("details", "Verifique el formato de los datos enviados");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
