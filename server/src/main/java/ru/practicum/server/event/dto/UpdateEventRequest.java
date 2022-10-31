package ru.practicum.server.event.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    @NotNull
    long eventId;
    private boolean paid;
    long participantLimit;
    private String title;
}
