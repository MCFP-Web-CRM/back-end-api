package com.mcfuturepartners.crm.api.category.service;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;

import java.util.List;

public interface CategoryService {
    String createCategory(CategoryDto categoryDto);
    List<CategoryDto> getCategories();
}
