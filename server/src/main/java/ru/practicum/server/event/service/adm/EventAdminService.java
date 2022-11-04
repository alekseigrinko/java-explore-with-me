package ru.practicum.server.event.service.adm;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.AdminUpdateEventRequestDto;
import ru.practicum.server.event.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * интерфейс администратора по работе с данными событий
 * */
public interface EventAdminService {

    /**
     * метод получения списка событий по заданным параметрам
     * @param states - список статусов
     * @param categories - список ID категорий
     * @param rangeStart - дата начала периода поиска по дате начала события
     * @param rangeEnd - дата окончания периода поиска по дате начала события
     * @return список DTO с полными данными события
     * @see EventFullDto
     * */
    List<EventFullDto> getAllEvents(List<String> states, List<Integer> categories, List<Integer> users, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, PageRequest pageRequest);

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
