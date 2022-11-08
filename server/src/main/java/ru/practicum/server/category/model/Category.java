package ru.practicum.server.category.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Параметр имени категории
     * Не может быть пустым, а также должен быть уникальным
     * */
    @Column(name = "name", nullable = false, unique = true)
    String name;
}
