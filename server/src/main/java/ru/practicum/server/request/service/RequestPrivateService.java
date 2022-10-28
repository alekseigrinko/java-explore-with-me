package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.RequestDto;

import java.util.List;

public interface RequestPrivateService {

    RequestDto postRequest(long userId, long eventId);

    RequestDto canceledRequest(long requestId, long userId);

    List<RequestDto> getAllUsersRequests(long userId);

    List<RequestDto> getRequestsForEventByUser(long userId, long eventId);

    RequestDto confirmRequest(long userId, long eventId,  long requestId);

    RequestDto rejectRequest(long userId, long eventId, long requestId);
}
