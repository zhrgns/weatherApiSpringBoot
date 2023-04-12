package com.weatherapp.app01.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiExceptionTemplate> handleException(ConstraintViolationException exception) {

        ApiExceptionTemplate temp = getApiExceptionTemplate(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(temp,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiExceptionTemplate> handleException(RequestNotPermitted exception) {

        ApiExceptionTemplate temp = getApiExceptionTemplate(exception.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
        return new ResponseEntity<>(temp,HttpStatus.TOO_MANY_REQUESTS);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiExceptionTemplate> handleException(RuntimeException exception) {

        ApiExceptionTemplate temp = getApiExceptionTemplate(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(temp,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private ApiExceptionTemplate getApiExceptionTemplate(String message, HttpStatus status) {
        ApiExceptionTemplate template = new ApiExceptionTemplate();
        template.setExceptionMessage(message);
        template.setStatusCode(status.value());
        template.setExceptionDate(LocalDateTime.now());
        return template;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ApiExceptionTemplate {
        private int statusCode;
        private String exceptionMessage;
        private LocalDateTime exceptionDate;
    }
}
