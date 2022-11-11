package ru.practicum.server.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс модели "Event" для сохранения в репозиторий
 * @see ru.practicum.server.event.EventRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Параметр названия события
     * Не может быть пустым
     * */
    @Column(name = "title", nullable = false, length = 512)
    String title;

    /**
     * Параметр описание события
     * Не может быть пустым
     * */
    @Column(name = "description", nullable = false, length = 5120)
    String description;


    /**
     * Параметр аннотации события
     * Не может быть пустым
     * */
    @Column(name = "annotation", nullable = false, length = 5120)
    String annotation;

    /**
     * Параметр статуса события
     * Не может быть пустым
     * */
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    State state;

    /**
     * Параметр ID инициатора события
     * Не может быть пустым
     * */
    @Column(name = "initiator_id", nullable = false)
    long initiatorId;

    /**
     * Параметр ID категории события
     * Не может быть пустым
     * */
    @Column(name = "category_id")
    long categoryId;

    /**
     * Параметр, обозначающий платность события
     * */
    @Column(name = "is_paid")
    boolean paid;

    /**
     * Параметр, обозначающий доступность модерации события
     * */
    @Column(name = "is_request_moderation")
    boolean requestModeration;

    /**
     * Дата создания события
     * */
    @Column(name = "created")
    LocalDateTime createdOn;

    /**
     * Дата публикация события
     * */
    @Column(name = "published_date")
    LocalDateTime publishedOn;

    /**
     * Дата начала события
     * Не может быть пустым
     * */
    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    /**
     * Параметр ширины координат локации проведения события
     * Не может быть пустым
     * */
    @Column(name = "lat_location", nullable = false)
    float latLocation;

    /**
     * Параметр долготы координат локации проведения события
     * Не может быть пустым
     * */
    @Column(name = "lon_location", nullable = false)
    float lonLocation;

    /**
     * Параметр лимита участников события
     * */
    @Column(name = "participant_limit")
    long participantLimit;

    /**
     * Параметр достижения лимита участников события
     * */
    @Column(name = "is_participant_limit")
    boolean isLimit;

}
