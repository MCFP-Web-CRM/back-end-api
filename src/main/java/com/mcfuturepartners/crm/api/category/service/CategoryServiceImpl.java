package com.mcfuturepartners.crm.api.category.service;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
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

    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }


    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new FindException("Category "+ ErrorCode.RESOURCE_NOT_FOUND.getMsg()));

        category.removeConnectionWithCustomers();

        try{
            categoryRepository.deleteById(categoryId);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public CategoryDto updateCategory(Long categoryId,CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new FindException("Category "+ ErrorCode.RESOURCE_NOT_FOUND.getMsg()));

        category.setName(categoryDto.getCategoryName());

        try{
            category = categoryRepository.save(category);
        }catch (Exception e){
            throw e;
        }
        return modelMapper.map(category, CategoryDto.class);
    }
}
