package com.raxrot.back.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CategoryResponse {
    private String categoryId;
    private String name;
    private String description;
    private String bgColor;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
