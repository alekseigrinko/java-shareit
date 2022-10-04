package ru.practicum.gateway.exeption;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}

