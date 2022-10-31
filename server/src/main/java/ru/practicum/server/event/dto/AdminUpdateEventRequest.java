package ru.practicum.server.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.event.model.Location;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventRequest {
    private String annotation;
    private long category;
    private String description;
    private String eventDate;
    Location location;
    private boolean paid;
    long participantLimit;
    private boolean requestModeration;
    private String title;
}
