package ru.practicum.server.category.service.publ;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.category.dto.CategoryDto;

import java.util.List;

/**
 * публичный интерфейс по работе с данными категорий
 * */
public interface CategoryPublicService {

    /**
     * метод получения данных категории по ID
     * @param categoryId - DTO c данными новой категории
     * @return возвращает DTO-класс с данными категории
     * @see CategoryDto
     * */
    CategoryDto getCategory(long categoryId);

    /**
     * метод получения данных всех категорий
     * @return возвращает список DTO категорий
     * @see CategoryDto
     * */
    List<CategoryDto> getAllCategories(PageRequest pageRequest);
}
