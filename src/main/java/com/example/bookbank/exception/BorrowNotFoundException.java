package com.example.bookbank.exception;

public class BorrowNotFoundException extends RuntimeException{

    public BorrowNotFoundException(String message){

        super(message);
    }
}
