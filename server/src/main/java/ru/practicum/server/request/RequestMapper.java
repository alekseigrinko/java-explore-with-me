package ru.practicum.server.request;

import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated().toString(),
                request.getEvent(),
                request.getId(),
                request.getRequester(),
                request.getStatus().toString()
        );
    }

    public static Request toRequest(ParticipationRequestDto participationRequestDto) {
        return new Request(
                participationRequestDto.getId(),
                participationRequestDto.getRequester(),
                participationRequestDto.getEvent(),
                LocalDateTime.parse(participationRequestDto.getCreated(), formatter),
                Status.valueOf(participationRequestDto.getStatus())
        );
    }
}
