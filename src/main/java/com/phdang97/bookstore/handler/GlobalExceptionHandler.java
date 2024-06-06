package com.phdang97.bookstore.handler;

import com.phdang97.bookstore.exception.ImageException;
import com.phdang97.bookstore.exception.OrderException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handles {@link EntityNotFoundException} by creating an error response and returning it with a 404 status code.
     *
     * @param exp the {@link EntityNotFoundException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 404 status code
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exp, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * Handles {@link EntityExistsException} by creating an error response and returning it with a 409 status code.
     *
     * @param exp the {@link EntityExistsException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 409 status code
     */
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException exp, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    /**
     * Handles {@link MethodArgumentNotValidException} by creating an error response and returning it with a 400 status code.
     *
     * @param exp the {@link MethodArgumentNotValidException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 400 status code
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp, HttpServletRequest request ) {
        Set<String> errorMessages = exp.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message("this request is not valid")
                .details(errorMessages)
                .build();

        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Handles {@link ConstraintViolationException} by creating an error response and returning it with a 400 status code.
     *
     * @param exp the {@link ConstraintViolationException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 400 status code
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exp, HttpServletRequest request ) {
        Set<String> errorMessages = exp.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message("this request is not valid")
                .details(errorMessages)
                .build();

        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Handles {@link ImageException} by creating an error response and returning it with a 400 status code.
     *
     * @param exp the {@link ImageException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 400 status code
     */
    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidImageExtensionException(ImageException exp, HttpServletRequest request ) {

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Handles {@link BadCredentialsException} by creating an error response and returning it with a 401 status code.
     *
     * @param exp the {@link BadCredentialsException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 401 status code
     */
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exp, HttpServletRequest request ) {
//
//        ErrorResponse error = ErrorResponse.builder()
//                .dateTime(LocalDateTime.now())
//                .status(HttpStatus.UNAUTHORIZED.value())
//                .path(request.getServletPath())
//                .message(exp.getMessage())
//                .build();
//        log.error(exp.getMessage(), exp);
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(error);
//    }

    /**
     * Handles {@link OrderException} by creating an error response and returning it with a 400 status code.
     *
     * @param exp the {@link OrderException} that occurred
     * @param request the HTTP request that triggered the exception
     * @return a {@link ResponseEntity} containing the error response and a 400 status code
     */
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException exp, HttpServletRequest request ) {

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
