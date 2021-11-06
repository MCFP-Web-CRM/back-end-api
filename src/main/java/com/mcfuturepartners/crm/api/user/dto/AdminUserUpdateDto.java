package com.mcfuturepartners.crm.api.user.dto;

import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
public class AdminUserUpdateDto {
    @ApiModelProperty(position = 1, example = "사원 명") private String name;
    @ApiModelProperty(position = 2, example = "핸드폰 번호") private String phone;
    @ApiModelProperty(position = 3, example = "부서 ") private Long departmentId;
    @ApiModelProperty(position = 4, example = "부서 / 수정 시 필요 없음")private Department department;

    public User toEntity(){

        return User.builder()
                .name(name)
                .phone(phone)
                .department(department)
                .build();
    }
}
