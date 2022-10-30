package ru.practicum.server.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryMapper;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.exeption.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.category.CategoryMapper.toCategoryDto;

@Service
@Slf4j
public class CategoryPublicServiceImp implements CategoryPublicService {

    private final CategoryRepository categoryRepository;

    public CategoryPublicServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> getAllCategories(PageRequest pageRequest) {
        log.debug("Получен список категорий");
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        checkCategory(categoryId);
        log.debug("Предоставлены данные категории ID: " + categoryId);
        return toCategoryDto(categoryRepository.findById(categoryId).get());
    }

    public void checkCategory(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.warn("Категории ID: " + categoryId + ", не найдено!");
            throw new ObjectNotFoundException("Категории ID: " + categoryId + ", не найдено!");
        }
    }
}
