package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(long id);

    List<CategoryDto> find(Integer from, Integer size);

    CategoryDto getById(long catId);

}
