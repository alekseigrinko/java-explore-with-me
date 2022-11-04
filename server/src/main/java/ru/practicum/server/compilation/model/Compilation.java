package ru.practicum.server.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Параметр названия подборки
     * Не может быть пустым
     * */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Параметр, указывающий на размещение подборки на главной странице
     * Не может быть пустым
     * */
    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;
    /**
     * Параметр даты и времени создания подборки
     * */
    @Column(name = "created")
    private LocalDateTime created;
}
