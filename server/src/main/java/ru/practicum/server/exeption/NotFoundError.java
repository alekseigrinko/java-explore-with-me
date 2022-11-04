package ru.practicum.server.exeption;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация класса ApiError для исключения со статусом HttpStatus.NOT_FOUND
 * */
public class NotFoundError extends ApiError {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NotFoundError(String message) {
        super.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
        super.setReason("The required object was not found.");
        super.setMessage(message);
        super.setTimestamp(LocalDateTime.now().format(formatter));
    }
}
