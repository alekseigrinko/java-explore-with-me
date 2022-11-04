package ru.practicum.server.event.model;

/**
 * Список для сортировки статусов события
 * @see Event
 */
public enum State {

    /**
     * Ожидает публикацию
     * */
    PENDING,

    /**
     * Опубликовано
     * */
    PUBLISHED,

    /**
     * Отменено
     * */
    CANCELED
}
