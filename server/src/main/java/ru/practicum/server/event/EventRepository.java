package ru.practicum.server.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.event.model.Event;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            " where e.initiatorId = ?1" +
            " order by e.createdOn desc ")
    Page<Event> getAllUserEvents(long userId, PageRequest pageRequest);

    @Query(" select e from Event e where e.state = 'PUBLISHER' " +
            "and (upper(e.annotation) like upper(concat('%', ?1, '%')) "
            + "or upper(e.description) like upper(concat('%', ?1, '%')) ) " +
            "and e.categoryId = ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5 " +
            "and e.isLimit <> ?6 ")
    Page<Event> getPublicAllEvents(String text, long category, boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, boolean onlyAvailable, PageRequest pageRequest);

    @Query(" select e from Event e where e.state = 'PUBLISHER' " +
            "and (upper(e.annotation) like upper(concat('%', ?1, '%')) "
            + "or upper(e.description) like upper(concat('%', ?1, '%')) ) " +
            "and e.categoryId = ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.isLimit <> ?5 ")
    Page<Event> getPublicAllEventsWithoutRange(String text, long category, boolean paid, LocalDateTime localDateTime, boolean onlyAvailable, PageRequest pageRequest);

    @Query(" select e from Event e where (upper(e.state) like upper(concat('%', ?1, '%'))) " +
            "and e.categoryId = ?2 " +
            "and e.initiatorId = ?3 " +
            "and e.eventDate > ?4 " +
            "and e.eventDate < ?5 ")
    Page<Event> getEventsForAdmin(String state, long category, long user, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, PageRequest pageRequest);
}
