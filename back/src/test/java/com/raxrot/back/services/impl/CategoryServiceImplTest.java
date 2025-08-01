package com.raxrot.back.services.impl;

import com.raxrot.back.dtos.CategoryRequest;
import com.raxrot.back.dtos.CategoryResponse;
import com.raxrot.back.models.Category;
import com.raxrot.back.repositories.CategoryRepository;
import com.raxrot.back.services.FileUploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void create_shouldSaveCategory_whenValidRequest() {
        CategoryRequest request = CategoryRequest.builder()
                .name("Food")
                .description("Expenses for food")
                .bgColor("#FFF")
                .build();

        MultipartFile file = new MockMultipartFile("file", "food.jpg", "image/jpeg", "fake".getBytes());

        Category category = new Category();
        Category savedCategory = new Category();
        savedCategory.setCategoryId("uuid-123");
        savedCategory.setImageUrl("https://s3/image.jpg");

        when(categoryRepository.existsByName("Food")).thenReturn(false);
        when(modelMapper.map(request, Category.class)).thenReturn(category);
        when(fileUploadService.uploadFile(file)).thenReturn("https://s3/image.jpg");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(modelMapper.map(savedCategory, CategoryResponse.class)).thenReturn(new CategoryResponse());

        CategoryResponse response = categoryService.create(request, file);

        assertNotNull(response);
        verify(categoryRepository).save(category);
    }

    @Test
    void getAllCategories_shouldReturnListOfCategoryResponses() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(any(Category.class), eq(CategoryResponse.class)))
                .thenReturn(new CategoryResponse());

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
    }

    @Test
    void deleteCategoryById_shouldDeleteCategoryAndImage() {
        String id = "abc123";
        Category category = new Category();
        category.setImageUrl("https://s3/image.jpg");

        when(categoryRepository.findByCategoryId(id)).thenReturn(Optional.of(category));

        categoryService.deleteCategoryById(id);

        verify(fileUploadService).deleteFile("https://s3/image.jpg");
        verify(categoryRepository).delete(category);
    }
}
