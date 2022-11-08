package ru.practicum.server.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.service.RequestPrivateService;

import java.util.List;

/**
 * Контроллер пользователя для работы с данными запросов на участие в событии
 * */
@RestController
@Slf4j
@RequestMapping(path = "users/{userId}/requests")
public class PrivateRequestController {

    /**
     * @see RequestPrivateService - интерефейс методов пользователя по работе с запросами на участие в событиях
     * */
    private final RequestPrivateService requestPrivateService;

    public PrivateRequestController(RequestPrivateService requestPrivateService) {
        this.requestPrivateService = requestPrivateService;
    }

    /**
     * Размещение данных нового запроса на участие в событии
     * */
    @PostMapping
    ParticipationRequestDto postRequest(@PathVariable long userId,
                                        @RequestParam long eventId) {
        log.debug("Получен запрос на размещение данных нового запроса на участие в событии");
        return requestPrivateService.postRequest(userId, eventId);
    }

    /**
     * Отмена пользователем запроса на участие в событии
     * */
    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto updateCategory(@PathVariable long userId,
                                           @PathVariable long requestId) {
        log.debug("Получен запрос на отмену пользователем запроса на участие в событии");
        return requestPrivateService.canceledRequest(requestId, userId);
    }

    /**
     * Получение списка запросов пользователя по его ID на участие в событиях
     * */
    @GetMapping
    List<ParticipationRequestDto> getUserRequest(@PathVariable long userId) {
        log.debug("Получен запрос на получение списка запросов пользователя по его ID на участие в событиях");
        return requestPrivateService.getAllUsersRequests(userId);
    }
}
