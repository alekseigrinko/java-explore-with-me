package ru.practicum.server.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.server.event.model.Event;

import java.time.LocalDateTime;

/**
 * Интерфейс для работы с репозиторием событий
 * @see Event
 * */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    /**
     * Метод получения всех событий, инициированных пользователем по ID
     * @param userId - ID пользователя
     * @return возвращает страницу событий, инициированных пользователем
     * @see Event
     * */
    @Query("select e from Event e " +
            " where e.initiatorId = ?1" +
            " order by e.createdOn desc ")
    Page<Event> getAllUserEvents(long userId, PageRequest pageRequest);

    /**
     * Метод получения всех событий по заданным параметрам
     * @param state - статус события
     * @param category - ID категории
     * @param user - ID пользователя
     * @param rangeStart - дата начала периода поиска по дате начала события
     * @param rangeEnd - дата окончания периода поиска по дате начала события
     * @return возвращает страницу событий по заданным параметрам
     * @see Event
     * */
    @Query(" select e from Event e where (upper(e.state) like upper(concat('%', ?1, '%'))) " +
            "and e.categoryId = ?2 " +
            "and e.initiatorId = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5 ")
    Page<Event> getEventsForAdmin(String state, long category, long user, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, PageRequest pageRequest);
}
