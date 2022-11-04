package ru.practicum.server.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Класс модели "Category" для сохранения в репозиторий
 * @see ru.practicum.server.category.CategoryRepository
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Параметр имени категории
     * Не может быть пустым, а также должен быть уникальным
     * */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
