package com.mcfuturepartners.crm.api.user.dto;

import com.mcfuturepartners.crm.api.department.dto.DepartmentResponseDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UserResponseDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String username;
    @ApiModelProperty(position = 3) private String name;
    @ApiModelProperty(position = 4) private String phone;
    @ApiModelProperty(position = 5)private DepartmentResponseDto department;
    @ApiModelProperty(position = 6) private String Authority;
}
