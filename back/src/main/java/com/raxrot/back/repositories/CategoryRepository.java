package com.raxrot.back.repositories;

import com.raxrot.back.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
   boolean existsByName(String name);
   Optional<Category> findByCategoryId(String categoryId);
}
