package ru.practicum.server.exeption;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}

