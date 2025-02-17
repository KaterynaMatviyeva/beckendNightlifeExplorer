package com.nightlifeexplorer.beckend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequestException(BadRequestException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(ElementNotFoundException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleForbiddenException(ForbiddenException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGenericException(Exception ex) {
        return new ErrorDTO("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}