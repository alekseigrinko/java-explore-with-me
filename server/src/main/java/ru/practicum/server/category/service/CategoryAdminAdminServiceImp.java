package ru.practicum.server.category.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.exeption.ObjectNotFoundException;

import static ru.practicum.server.category.CategoryMapper.toCategory;
import static ru.practicum.server.category.CategoryMapper.toCategoryDto;

@Service
@Slf4j
public class CategoryAdminAdminServiceImp implements CategoryAdminService {

    private final CategoryRepository categoryRepository;

    public CategoryAdminAdminServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        return toCategoryDto(categoryRepository.save(toCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(long categoryId, CategoryDto categoryDto) {
        checkCategory(categoryId);
        Category categoryInMemory = categoryRepository.findById(categoryId).get();
        if (categoryDto.getName() != null) {
            categoryInMemory.setName(categoryDto.getName());
        }
        log.debug("Категория обновлена: " + categoryInMemory);
        return toCategoryDto(categoryRepository.save(categoryInMemory));
    }

    @Override
    public CategoryDto deleteCategory(long categoryId) {
        CategoryDto categoryDto = toCategoryDto(categoryRepository.findById(categoryId).get());
        categoryRepository.deleteById(categoryId);
        return categoryDto;
    }

    public void checkCategory(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            log.warn("Категории ID: " + categoryId + ", не найдено!");
            throw new ObjectNotFoundException("Категории ID: " + categoryId + ", не найдено!");
        }
    }
}
