package com.BBVA.DiMo_S1.E_exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
}
