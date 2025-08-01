package com.raxrot.back.services.impl;

import com.raxrot.back.dtos.CategoryRequest;
import com.raxrot.back.dtos.CategoryResponse;
import com.raxrot.back.exceptions.ApiException;
import com.raxrot.back.models.Category;
import com.raxrot.back.repositories.CategoryRepository;
import com.raxrot.back.services.CategoryService;
import com.raxrot.back.services.FileUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileUploadService fileUploadService;
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, FileUploadService fileUploadService) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public CategoryResponse create(CategoryRequest request, MultipartFile file) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ApiException("Category with name " + request.getName() + " already exists");
        }

        Category category = modelMapper.map(request, Category.class);

        String imageUrl=fileUploadService.uploadFile(file);
        String categoryId= UUID.randomUUID().toString();
        category.setCategoryId(categoryId);
        category.setImageUrl(imageUrl);

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoriesResponse = categories.stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
        return categoriesResponse;
    }

    @Override
    public void deleteCategoryById(String categoryId) {
        Category category=categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(()-> new ApiException("Category with id " + categoryId + " does not exist"));
        fileUploadService.deleteFile(category.getImageUrl());
        categoryRepository.delete(category);
    }
}
