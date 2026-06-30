package com.CMTS.inventory.controller;

import com.CMTS.inventory.domain.dto.ErrorDto;
import com.CMTS.inventory.exception.ArchivedProductionException;
import com.CMTS.inventory.exception.ItemNotAvailableException;
import com.CMTS.inventory.exception.ResourceNotFoundException;
import com.CMTS.inventory.exception.ProductionNotArchivableException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    public ResponseEntity<ErrorDto> handleItemNotAvailableException(ItemNotAvailableException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed.");
        ErrorDto errorDto = new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductionNotArchivableException.class)
    public ResponseEntity<ErrorDto> ProductionNotArchivableException(ProductionNotArchivableException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ArchivedProductionException.class)
    public ResponseEntity<ErrorDto> handleArchivedProductionException(ArchivedProductionException ex) {
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }
}