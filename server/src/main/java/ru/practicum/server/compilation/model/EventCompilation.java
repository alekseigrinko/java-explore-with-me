package ru.practicum.server.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "event_compilations")
public class EventCompilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID события
     * Не может быть пустым
     * */
    @Column(name = "event_id", nullable = false)
    private long eventId;

    /**
     * ID подборки
     * Не может быть пустым
     * */
    @Column(name = "compilation_id", nullable = false)
    private long compilationId;
}
