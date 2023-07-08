/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.time.LocalDateTime;

/**
 * @author Lenovo
 */
@ControllerAdvice
public class ExceptionHandlers extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ApiError> handleException(AuthenticationException e, HttpServletRequest request) {
        String message = e.getMessage().equals("Bad credentials") ? "Password is incorrect" : e.getMessage();
        ApiError apiError = ApiError
                .builder()
                .path(request.getRequestURI())
                .exceptionMessage(message)
                .message(message)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ApiError> IlegalException(IllegalStateException e, HttpServletRequest request) {
        ApiError apiError = ApiError
                .builder()
                .path(request.getRequestURI())
                .exceptionMessage(e.getMessage())
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ApiError> UsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
        ApiError apiError = ApiError
                .builder()
                .path(request.getRequestURI())
                .exceptionMessage(e.getMessage())
                .message(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
