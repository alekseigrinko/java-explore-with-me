package ru.practicum.server.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс модели "Compilation" для сохранения в репозиторий
 * @see ru.practicum.server.compilation.repository.CompilationRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Параметр названия подборки
     * Не может быть пустым
     * */
    @Column(name = "title", nullable = false, length = 512)
    String title;

    /**
     * Параметр, указывающий на размещение подборки на главной странице
     * Не может быть пустым
     * */
    @Column(name = "is_pinned", nullable = false)
    boolean pinned;
    /**
     * Параметр даты и времени создания подборки
     * */
    @Column(name = "created")
    LocalDateTime created;
}
