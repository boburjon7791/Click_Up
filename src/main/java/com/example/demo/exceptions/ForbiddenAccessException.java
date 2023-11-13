package com.example.demo.exceptions;

public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String message) {
        super(message);
    }
}
