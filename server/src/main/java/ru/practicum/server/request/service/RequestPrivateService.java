package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {

    ParticipationRequestDto postRequest(long userId, long eventId);

    ParticipationRequestDto canceledRequest(long requestId, long userId);

    List<ParticipationRequestDto> getAllUsersRequests(long userId);

    List<ParticipationRequestDto> getRequestsForEventByUser(long userId, long eventId);

    ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId);

    ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId);
}
