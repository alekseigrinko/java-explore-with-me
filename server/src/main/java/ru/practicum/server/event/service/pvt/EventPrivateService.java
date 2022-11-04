package ru.practicum.server.event.service.pvt;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.NewEventDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.dto.UpdateEventRequestDto;

import java.util.List;

/**
 * интерфейс пользователя по работе с данными событий
 * */
public interface EventPrivateService {

    /**
     * метод получения списка событий пользователя по ID пользователя
     * @param userId - ID пользователя
     * @return список DTO с полными данными события
     * @see EventFullDto
     * */
    List<EventFullDto> getAllUserEvents(long userId, PageRequest pageRequest);

    /**
     * метод получения события по его ID
     * @param userId - ID пользователя
     * @param eventId - ID события
     * @return DTO с полными данными события
     * @see EventFullDto
     * */
    EventFullDto getUserEventById(long userId, long eventId);

    /**
     * метод сохранения данных события
     * @param userId - ID пользователя
     * @param newEventDto - DTO с данными нового события
     * @return DTO с полными данными события
     * @see NewEventDto
     * @see EventFullDto
     * */
    EventFullDto addEvent(long userId, NewEventDto newEventDto);

    /**
     * метод обновления данных события пользователем
     * @param userId - ID пользователя
     * @param updateEventRequestDto - DTO с данными нового события
     * @return DTO с полными данными события
     * @see UpdateEventRequestDto
     * @see EventFullDto
     * */
    EventFullDto updateEvent(long userId, UpdateEventRequestDto updateEventRequestDto);

    /**
     * метод отмены события
     * @param userId - ID пользователя
     * @param eventId - ID события
     * @return DTO с полными данными события
     * @see EventFullDto
     * */
    EventFullDto cancelEvent(long userId, long eventId);
}
