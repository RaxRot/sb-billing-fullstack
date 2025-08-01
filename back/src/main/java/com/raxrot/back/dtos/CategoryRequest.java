package com.raxrot.back.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private String bgColor;
}
