package com.arturoroes.minierp.service;

import com.arturoroes.minierp.dto.CategoryRequestDto;
import com.arturoroes.minierp.dto.CategoryResponseDto;
import com.arturoroes.minierp.entity.Category;
import com.arturoroes.minierp.exception.DuplicateResourceException;
import com.arturoroes.minierp.exception.ResourceNotFoundException;
import com.arturoroes.minierp.mapper.CategoryMapper;
import com.arturoroes.minierp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return categoryMapper.toResponseDto(category);
    }

    public CategoryResponseDto create(CategoryRequestDto dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Category", "name", dto.getName());
        }
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDto(saved);
    }

    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (!category.getName().equals(dto.getName())
                && categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Category", "name", dto.getName());
        }

        categoryMapper.updateEntity(category, dto);
        Category updated = categoryRepository.save(category);
        return categoryMapper.toResponseDto(updated);
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}