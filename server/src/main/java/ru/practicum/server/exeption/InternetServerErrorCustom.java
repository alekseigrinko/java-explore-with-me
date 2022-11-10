package ru.practicum.server.exeption;

/**
 * Реализация класса ApiError для исключения со статусом HttpStatus.BAD_REQUEST
 * */

public class InternetServerErrorCustom extends ApiErrorCustom {

    public InternetServerErrorCustom(String status, String reason, String message, String timestamp, StackTraceElement[] errors) {
        super(status, reason, message, timestamp, errors);
    }
}
