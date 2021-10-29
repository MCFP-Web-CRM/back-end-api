package com.mcfuturepartners.crm.api.category.service;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public String createCategory(CategoryDto categoryDto) {
        try{
            log.info(categoryDto.toEntity().toString());
            categoryRepository.save(categoryDto.toEntity());
            return "save successful";
        }catch(Exception e){
            throw e;
        }

    }
}
