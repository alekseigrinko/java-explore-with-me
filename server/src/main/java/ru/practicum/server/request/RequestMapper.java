package ru.practicum.server.request;

import ru.practicum.server.request.dto.RequestDto;
import ru.practicum.server.request.model.Request;

public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getRequester(),
                request.getEvent(),
                request.getCreated(),
                request.getStatus()
        );
    }

    public static Request toRequest(RequestDto requestDto) {
        return new Request(
                requestDto.getId(),
                requestDto.getRequester(),
                requestDto.getEvent(),
                requestDto.getCreated(),
                requestDto.getStatus()
        );
    }
}
