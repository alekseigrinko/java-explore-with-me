package ru.practicum.server.exeption;

/**
 * Реализация класса ApiError для исключения со статусом HttpStatus.BAD_REQUEST
 * */

public class BadRequestErroCustom extends ApiErrorCustom {

    public BadRequestErroCustom(String status, String reason, String message, String timestamp, StackTraceElement[] errors) {
        super(status, reason, message, timestamp, errors);
    }
}
