package ru.practicum.server.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Абстрактный класс для реализации шаблона исключения
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ApiErrorCustom {
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

    /**
     * StackTrace ошибки
     * */
    private StackTraceElement[] errors;
}
