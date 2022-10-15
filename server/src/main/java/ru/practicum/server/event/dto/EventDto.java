package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.event.model.State;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String application;
    private State state;
    private long initiatorId;
    private long categoryId;
    private boolean paid;
    private boolean requestModeration;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private LocalDateTime eventDate;
    LocationDto locationDto;
}

@Getter
@AllArgsConstructor
class LocationDto {
    private float lat;
    private float lon;
}
