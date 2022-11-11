package ru.practicum.server.event.service.adm;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.AdminUpdateEventRequestDto;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.AdminEventFilter;

import java.util.List;

/**
 * интерфейс администратора по работе с данными событий
 * */
public interface EventAdminService {

    /**
     * метод получения списка событий по заданным параметрам
     * @param adminEventFilter - список параметров запроса
     * @return список DTO с полными данными события
     * @see EventFullDto
     * */
    List<EventFullDto> getAllEvents(AdminEventFilter adminEventFilter, PageRequest pageRequest);

    /**
     * метод обновления данных события администратором
     * @param eventId - ID события
     * @param adminUpdateEventRequestDto - DTO с данными для обновления события
     * @return DTO с обновленными полными данными события
     * @see AdminUpdateEventRequestDto
     * @see EventFullDto
     * */
    EventFullDto updateEvent(long eventId, AdminUpdateEventRequestDto adminUpdateEventRequestDto);

    /**
     * метод публикации события
     * @param eventId - ID события
     * @return DTO с полными данными события
     * @see EventFullDto
     * */
    EventFullDto publishEvent(long eventId);

    /**
     * метод отмены публикации события
     * @param eventId - ID события
     * @return DTO с полными данными события
     * @see EventFullDto
     * */
    EventFullDto rejectEvent(long eventId);
}
