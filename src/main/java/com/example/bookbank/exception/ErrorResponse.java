package com.example.bookbank.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timeStamp;
    private Map<String, String> errors;

    public ErrorResponse(
            int status,
            String message,
            LocalDateTime timeStamp) {

        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public ErrorResponse(
            int status,
            String message,
            LocalDateTime timeStamp,
            Map<String, String> errors) {

        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
        this.errors = errors;
    }

    // Getters and setters
}