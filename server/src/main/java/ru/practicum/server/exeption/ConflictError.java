package ru.practicum.server.exeption;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация класса ApiError для исключения со статусом HttpStatus.CONFLICT
 * */
public class ConflictError extends ApiError {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ConflictError(String message) {
        super.setStatus(String.valueOf(HttpStatus.CONFLICT));
        super.setReason("Integrity constraint has been violated");
        super.setMessage(message);
        super.setTimestamp(LocalDateTime.now().format(formatter));
    }
}
