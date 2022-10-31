package ru.practicum.server.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "annotation")
    private String annotation;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "initiator_id")
    private long initiatorId;
    @Column(name = "category_id")
    private long categoryId;
    @Column(name = "is_paid")
    private boolean paid;
    @Column(name = "is_request_moderation")
    private boolean requestModeration;
    @Column(name = "created")
    private LocalDateTime createdOn;
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "lat_location")
    private float latLocation;
    @Column(name = "lon_location")
    private float lonLocation;
    @Column(name = "participant_limit")
    private long participantLimit;
    @Column(name = "is_participant_limit")
    private boolean isLimit;

}
