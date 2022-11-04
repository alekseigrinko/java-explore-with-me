package ru.practicum.server.event.service.publ;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.event.dto.EventFullDto;
import ru.practicum.server.event.model.SortEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * публичный интерфейс по работе с данными событий
 * */
public interface EventPublicService {

    /**
     * метод получения списка событий по заданным параметрам
     * @param text - текстовый запрос, который должен содержаться в аннотации и описании события
     * @param categories - список ID категорий
     * @param paid - условие платности события
     * @param rangeStart - дата начала периода поиска по дате начала события
     * @param rangeEnd - дата окончания периода поиска по дате начала события
     * @param onlyAvailable - условия для событий, у которых не исчерпан лимит запросов на участие
     * @return список DTO с полными данными события
     * @see EventFullDto
     * */
    List<EventFullDto> getAllEvents(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort, PageRequest pageRequest);

    /**
     * метод получения данных события по ID
     * @param eventId - ID события
     * @return DTO с полными данными события
     * @see EventFullDto
     * */
    EventFullDto getEvent(long eventId);
}
