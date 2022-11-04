package ru.practicum.server.exeption;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация класса ApiError для исключения со статусом HttpStatus.FORBIDDEN
 * */
public class ForbiddenError extends ApiError {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ForbiddenError(String message) {
        super.setStatus(String.valueOf(HttpStatus.FORBIDDEN));
        super.setReason("For the requested operation the conditions are not met.");
        super.setMessage(message);
        super.setTimestamp(LocalDateTime.now().format(formatter));
    }
}
