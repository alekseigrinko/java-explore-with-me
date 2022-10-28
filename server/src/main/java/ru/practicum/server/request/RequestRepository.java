package ru.practicum.server.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select count (r) from Request r " +
            " where r.requester = ?1 and r.status = 'CONFIRMED'")
    Integer getEventParticipantLimit(long eventId);


    List<Request> getAllByRequester(long requesterId);

    List<Request> getAllByRequesterAndAndEvent(long requesterId, long eventId);
}
