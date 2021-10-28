package com.mcfuturepartners.crm.api.category.dto;

import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.department.entity.Department;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class CategoryDto {

    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String categoryName;

    public Category toEntity(){
        return Category.builder()
                .name(categoryName)
                .build();
    }
}
