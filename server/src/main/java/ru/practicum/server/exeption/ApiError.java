package ru.practicum.server.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Абстрактный класс для реализации шаблона исключения
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ApiError extends RuntimeException {
    /**
     * Код статуса HTTP-ответа
     * */
    private String status;

    /**
     * Общее описание причины ошибки
     * */
    private String reason;

    /**
     * Сообщение об ошибке
     * */
    private String message;

    /**
     * Время и дата ошибки
     * */
    private String timestamp;
}
