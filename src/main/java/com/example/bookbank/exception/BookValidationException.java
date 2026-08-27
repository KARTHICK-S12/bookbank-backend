package com.example.bookbank.exception;

public class BookValidationException extends RuntimeException{

    public BookValidationException(String message) {
        super(message);
    }
}
