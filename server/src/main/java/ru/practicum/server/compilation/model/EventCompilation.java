package ru.practicum.server.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * Класс модели для сохранения данных связки ключа события и подборки в репозиторий
 * @see ru.practicum.server.compilation.repository.EventCompilationRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "event_compilations")
public class EventCompilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    /**
     * ID события
     * Не может быть пустым
     * */
    @Column(name = "event_id", nullable = false)
    long eventId;

    /**
     * ID подборки
     * Не может быть пустым
     * */
    @Column(name = "compilation_id", nullable = false)
    long compilationId;
}
