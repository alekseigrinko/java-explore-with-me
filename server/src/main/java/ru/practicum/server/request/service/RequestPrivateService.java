package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.ParticipationRequestDto;

import java.util.List;

/**
 * интерфейс пользователя по работе с данными запросов на участие в событиях
 * */
public interface RequestPrivateService {

    /**
     * метод размещения запроса на участие в событии
     * @param userId - ID пользователя, который размещает запрос
     * @param eventId - ID события, на участие в котором размещается запрос
     * @return возвращает DTO с данными зарегистрированного запроса
     * @see ParticipationRequestDto
     * */
    ParticipationRequestDto postRequest(long userId, long eventId);

    /**
     * метод отмены пользователем запроса на участие в событии
     * @param requestId - ID пользователя, который разместил запрос
     * @param userId - ID пользователя, который подал запрос на отмену запроса
     * @return возвращает DTO с данными запроса
     * @see ParticipationRequestDto
     * */
    ParticipationRequestDto canceledRequest(long requestId, long userId);

    /**
     * метод получения списка запросов пользователя по его ID на участие в событиях
     * @param userId - ID пользователя
     * @return возвращает список DTO с данными запросов
     * @see ParticipationRequestDto
     * */
    List<ParticipationRequestDto> getAllUsersRequests(long userId);

    /**
     * метод списка запросов на участие в событии
     * @param userId - ID пользователя, который был инициатором события
     * @param eventId - ID события
     * @return возвращает список DTO с данными запросо
     * @see ParticipationRequestDto
     * */
    List<ParticipationRequestDto> getRequestsForEventByUser(long userId, long eventId);

    /**
     * метод подтверждение пользователем запроса на участие в событии
     * @param requestId - ID запроса
     * @param userId - ID пользователя - инициатора события
     * @param eventId - ID события, на участие в котором размещается запрос
     * @return возвращает DTO с данными запроса
     * @see ParticipationRequestDto
     * */
    ParticipationRequestDto confirmRequest(long userId, long eventId, long requestId);

    /**
     * метод отклонения пользователем запроса на участие в событии
     * @param requestId - ID запроса
     * @param userId - ID пользователя - инициатора события
     * @param eventId - ID события, на участие в котором размещается запрос
     * @return возвращает DTO с данными запроса
     * @see ParticipationRequestDto
     * */
    ParticipationRequestDto rejectRequest(long userId, long eventId, long requestId);
}
