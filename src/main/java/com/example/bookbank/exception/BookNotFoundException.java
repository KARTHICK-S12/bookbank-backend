package com.example.bookbank.exception;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(String message){

        super(message);
    }
}
