package com.mcfuturepartners.crm.api.department.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentResponseDto {
    @ApiModelProperty(position = 0, example = "부서 ID, 추가 요청 시 불필요") private Long id;
    @ApiModelProperty(position = 1, example = "부서명") private String departmentName;
}
