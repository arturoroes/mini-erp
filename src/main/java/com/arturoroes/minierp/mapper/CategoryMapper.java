package com.arturoroes.minierp.mapper;

import com.arturoroes.minierp.dto.CategoryRequestDto;
import com.arturoroes.minierp.dto.CategoryResponseDto;
import com.arturoroes.minierp.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDto dto) {
        Category category= new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }

    public void updateEntity(Category category, CategoryRequestDto dto){
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }

    public CategoryResponseDto toResponseDto(Category category){
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
