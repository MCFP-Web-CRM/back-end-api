package com.mcfuturepartners.crm.api.admin.dto;

import com.mcfuturepartners.crm.api.department.entity.Department;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentDto {

    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String departmentName;

    public Department toEntity(){
        return Department.builder()
                .name(departmentName)
                .build();
    }
}
