package com.example.demo.exceptions;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Configuration
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handler(IllegalArgumentException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handler(BadRequestException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ForbiddenAccessException.class)
    public ResponseEntity<Object> handler(ForbiddenAccessException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handler(NotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
}
