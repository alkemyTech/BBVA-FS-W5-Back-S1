package com.BBVA.DiMo_S1.E_exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException customException) {
        Map<String, Object> response = new HashMap<>();
        response.put("Fecha y Hora del errror:", LocalDateTime.now().toString());
        response.put("Nombre del error:", customException.getStatus().name());
        response.put("Estado:", customException.getStatus().value());
        response.put("Mensaje:", customException.getMessage());
        return ResponseEntity.status(customException.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception exception) {
        Map<String, Object> response = new HashMap<>();
        response.put("Fecha y Hora del errror:", LocalDateTime.now().toString());
        response.put("Causa del error:", exception.getCause());
        response.put("Mensaje:", exception.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("Fecha y Hora del error:", LocalDateTime.now().toString());
        response.put("Estado:", HttpStatus.BAD_REQUEST.value());
        response.put("Errores de validación:", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> response = new HashMap<>();

        // Concatenar mensajes de error si hay varias violaciones en el mismo campo
        Map<String, String> violations = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (existing, replacement) -> existing + "; " + replacement // Combina mensajes de error
                ));

        response.put("Fecha y Hora del error", LocalDateTime.now().toString());
        response.put("Estado", HttpStatus.BAD_REQUEST.value());
        response.put("Errores de validación", violations);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("Fecha y Hora del error:", LocalDateTime.now().toString());
        response.put("Estado:", HttpStatus.BAD_REQUEST.value());
        response.put("Mensaje:", "El tipo de cuenta no es valido. Debe ser ARS o USD");
        return ResponseEntity.badRequest().body(response);
    }
}
