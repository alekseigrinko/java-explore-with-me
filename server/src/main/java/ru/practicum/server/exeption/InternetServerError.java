package ru.practicum.server.exeption;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация класса ApiError для исключения со статусом HttpStatus.INTERNAL_SERVER_ERROR
 * */
public class InternetServerError extends ApiError {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public InternetServerError(String message) {
        super.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        super.setReason("Error occurred");
        super.setMessage(message);
        super.setTimestamp(LocalDateTime.now().format(formatter));
    }
}
