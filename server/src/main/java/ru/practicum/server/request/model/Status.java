package ru.practicum.server.request.model;

/**
 * Список статусов запросов на участие в событии
 * @see Request
 */
public enum Status {
    /**
     * Ожидает ответа
     * */
    PENDING,

    /**
     * Подтвержден
     * */
    CONFIRMED,

    /**
     * Отменен инициатором
     * */
    CANCELED,

    /**
     * Отказано в участии
     * */
    REJECTED
}
