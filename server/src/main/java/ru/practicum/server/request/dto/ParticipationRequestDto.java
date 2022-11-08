package ru.practicum.server.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO-класс с данными заявки на участие в событии
 * @see ru.practicum.server.request.model.Request
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    String created;
    long event;
    Long id;
    long requester;
    String status;
}
