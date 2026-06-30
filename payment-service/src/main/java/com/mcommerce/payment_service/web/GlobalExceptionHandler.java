package com.mcommerce.payment_service.web;

import com.mcommerce.payment_service.dto.ApiError;
import com.mcommerce.payment_service.exception.EmptyCartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Erreur métier : panier vide
     */
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiError> handleEmptyCartException(EmptyCartException ex) {

        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Erreur générique (fallback)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {

        ApiError error = ApiError.builder()
                .message("Erreur interne du serveur")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
