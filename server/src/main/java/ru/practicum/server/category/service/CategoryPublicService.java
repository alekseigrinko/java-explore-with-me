package ru.practicum.server.category.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.server.category.dto.CategoryDto;

import java.util.List;

public interface CategoryPublicService {

    CategoryDto getCategory(long categoryId);

    List<CategoryDto> getAllCategories(PageRequest pageRequest);
}
