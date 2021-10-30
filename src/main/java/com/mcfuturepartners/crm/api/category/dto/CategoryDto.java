package com.mcfuturepartners.crm.api.category.dto;

import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.department.entity.Department;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor @Data
public class CategoryDto {

    @ApiModelProperty(position = 0, example = "카테고리 ID (category 추가 요청 시 필요 없음)") @Nullable private long id;
    @ApiModelProperty(position = 1, example = "카테고리 명") private String categoryName;

    public Category toEntity(){
        return Category.builder()
                .name(categoryName)
                .build();
    }
}
