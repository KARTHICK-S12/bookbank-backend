package com.example.bookbank.exception;

public class ConflictException extends RuntimeException{

    public ConflictException(String message){
        super(message);
    }
}
