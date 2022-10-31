package ru.practicum.server.exeption;

import java.util.List;

public class ApiError extends RuntimeException {
    private StackTraceElement [] errors;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

    public ApiError(StackTraceElement [] errors, String message, String reason, String status, String timestamp) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}

