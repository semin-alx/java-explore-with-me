package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class CategoryTests {

    private final EntityManager em;
    private final CategoryService categoryService;

    @Test
    public void createAndGetCategory() {
        CategoryDto categoryDto = new CategoryDto(null, "category1");
        categoryDto = categoryService.create(categoryDto);

        TypedQuery<Category> query = em.createQuery("Select c from Category c where c.name = :name",
                Category.class);

        Category category = query.setParameter("name", categoryDto.getName()).getSingleResult();

        Assertions.assertTrue(category != null);
        Assertions.assertEquals(category.getId(), categoryDto.getId());

    }

}
