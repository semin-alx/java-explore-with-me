package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.helper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryReposirory;
import ru.practicum.common.error_handling.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryReposirory categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {

        if ((categoryDto.getId() != null) && (categoryDto.getId() != 0)) {
            throw new IllegalArgumentException("При создании категории id должен быть 0");
        }

        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);

    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {

        if ((categoryDto.getId() == null) || (categoryDto.getId() == 0)) {
            throw new IllegalArgumentException("Не указан id категории");
        }

        Category category = CategoryMapper.toCategory(categoryDto);
        category = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(category);

    }

    @Override
    public void deleteById(long id) {

        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("Категория с таким id в базе не найдена");
        }

    }

    @Override
    public List<CategoryDto> find(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long catId) {

        Optional<Category> category = categoryRepository.findById(catId);

        if (!category.isPresent()) {
            throw new ObjectNotFoundException("Указанная категория не найдена");
        }

        return CategoryMapper.toCategoryDto(category.get());

    }
}
