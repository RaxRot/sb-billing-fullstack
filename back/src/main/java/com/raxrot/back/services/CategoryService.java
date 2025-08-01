package com.raxrot.back.services;

import com.raxrot.back.dtos.CategoryRequest;
import com.raxrot.back.dtos.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request, MultipartFile file);
    List<CategoryResponse> getAllCategories();
    void deleteCategoryById(String categoryId);
}
