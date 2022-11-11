package ru.practicum.server.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.request.model.Request;

import java.util.List;

/**
 * Интерфейс для работы с репозиторием запросов на участие в событиях
 * @see Request
 * */
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Метод получения количества подтвержденных запросов по ID события
     * @param eventId - ID события
     * @return количество подтвержденных запросов на участие в событии
     * */
    @Query("select count (r) from Request r " +
            " where r.requester = ?1 and r.status = 'CONFIRMED'")
    Integer getEventParticipantLimit(long eventId);

    /**
     * Метод получения списка запросов на участие в событиях по ID инициатора запроса
     * @param requesterId - ID инициатора запроса
     * @return список запросов инициированных пользователем
     * @see Request
     * */
    List<Request> getAllByRequester(long requesterId);

    /**
     * Метод получения списка запросов на участие в событии по ID события
     * @param event - ID события
     * @return список запросов на участие в событии
     * @see Request
     * */
    List<Request> findAllByEvent(long event);

    /**
     * Метод получения запроса на участие в событии по ID пользователя и события
     * @param event - ID события
     * @param requester - ID инициатора запроса
     * @return запрос на участие в событии
     * @see Request
     * */
    Request findByEventAndRequester(long event, long requester);
}
