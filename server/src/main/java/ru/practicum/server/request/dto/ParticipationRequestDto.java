package ru.practicum.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO-класс с данными заявки на участие в событии
 * @see ru.practicum.server.request.model.Request
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private String created;
    private long event;
    private Long id;
    private long requester;
    private String status;
}
