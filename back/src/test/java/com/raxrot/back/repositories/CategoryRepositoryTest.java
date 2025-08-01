package com.raxrot.back.repositories;

import com.raxrot.back.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Test
    void testExistsByName() {
        // given
        Category category = new Category();
        category.setName("Electronics");
        category.setCategoryId(UUID.randomUUID().toString());
        categoryRepository.save(category);

        // when
        boolean exists = categoryRepository.existsByName("Electronics");

        // then
        assertTrue(exists);
    }

    @Test
    void testFindByCategoryId() {
        // given
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category();
        category.setName("Books");
        category.setCategoryId(categoryId);
        categoryRepository.save(category);

        // when
        Optional<Category> found = categoryRepository.findByCategoryId(categoryId);

        // then
        assertTrue(found.isPresent());
        assertEquals("Books", found.get().getName());
    }

}