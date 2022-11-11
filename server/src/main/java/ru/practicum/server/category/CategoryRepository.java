package ru.practicum.server.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.category.model.Category;

/**
 * Интерфейс для работы с репозиторием категорий
 * @see Category
 * */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
