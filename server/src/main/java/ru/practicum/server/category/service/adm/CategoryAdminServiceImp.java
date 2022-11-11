package ru.practicum.server.category.service.adm;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.CategoryRepository;
import ru.practicum.server.category.dto.CategoryDto;
import ru.practicum.server.category.dto.NewCategoryDto;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.exeption.BadRequestError;
import ru.practicum.server.exeption.NotFoundError;

import javax.validation.constraints.NotNull;

import static ru.practicum.server.category.CategoryMapper.toCategory;
import static ru.practicum.server.category.CategoryMapper.toCategoryDto;

/**
 * реализация интерфейса администратора по работе с данными категорий
 * @see CategoryAdminService
 * */
@Service
@Slf4j
public class CategoryAdminServiceImp implements CategoryAdminService {

    /**
     * репозиторий категорий
     * @see CategoryRepository
     * */
    private final CategoryRepository categoryRepository;

    public CategoryAdminServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * @see CategoryAdminService#addCategory(NewCategoryDto)
     * @param newCategoryDto - не должен быть null
     * */
    @Override
    public CategoryDto addCategory(@NonNull NewCategoryDto newCategoryDto) {
        log.debug("Добавлена категория " + newCategoryDto.getName());
        return toCategoryDto(categoryRepository.save(toCategory(newCategoryDto)));
    }

    /**
     * @see CategoryAdminService#updateCategory(CategoryDto)
     * @param categoryDto - не должен быть null
     * */
    @Override
    public CategoryDto updateCategory(@NotNull CategoryDto categoryDto) {
        if (categoryDto == null) {
            log.warn("Пустая информация для обновления категории");
            throw new BadRequestError("Пустая информация для обновления категории");
        }
        Category categoryInMemory = returnCategoryWithCheck(categoryDto.getId());
        if (categoryDto.getName() != null) {
            categoryInMemory.setName(categoryDto.getName());
        }
        log.debug("Категория обновлена: " + categoryInMemory);
        return toCategoryDto(categoryRepository.save(categoryInMemory));
    }

    /**
     * @see CategoryAdminService#deleteCategory(long)
     * */
    @Override
    public CategoryDto deleteCategory(long categoryId) {
        CategoryDto categoryDto = toCategoryDto(returnCategoryWithCheck(categoryId));
        categoryRepository.deleteById(categoryId);
        log.debug("Удалена категория ID: " + categoryId);
        return categoryDto;
    }

    /**
     * метод проверки наличия категории по ID в репозитории и ее получения
     * @return  возвращает категорию по ID
     * @throws NotFoundError - при отсутствии категории по ID
     * */
    public Category returnCategoryWithCheck(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> {
            log.warn("Категории ID: " + categoryId + ", не найдено!");
            throw new NotFoundError("Категории ID: " + categoryId + ", не найдено!");
        });
    }
}
