package ru.practicum.server.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "application")
    private String application;
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
    @Column(name = "location_id")
    long locationId;
}
