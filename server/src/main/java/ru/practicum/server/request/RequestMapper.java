package ru.practicum.server.request;

import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Маппер для работы с моделями и DTO-классами пакета request
 * */
public class RequestMapper {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Метод конвертации данных из репозитория в DTO
     * */
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated().format(formatter),
                request.getEvent(),
                request.getId(),
                request.getRequester(),
                request.getStatus().toString()
        );
    }

    /**
     * Метод конвертации данных из DTO в модель Request
     * */
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
