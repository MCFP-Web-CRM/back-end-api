package com.mcfuturepartners.crm.api.category.service;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;

public interface CategoryService {
    String createCategory(CategoryDto categoryDto);
}
