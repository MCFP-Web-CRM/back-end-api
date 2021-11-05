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
public class UserUpdateDto {
    @ApiModelProperty(position = 2, example = "사원 계정 비밀번호") private String password;
    @ApiModelProperty(position = 3, example = "사원 명") private String name;
    @ApiModelProperty(position = 4, example = "핸드폰 번호") private String phone;
    @ApiModelProperty(position = 5, example = "부서 ") private Long departmentId;
    @ApiModelProperty(position = 6, example = "부서 / 사용자 추가 시 필요 없음")private Department department;
    @ApiModelProperty(position = 7, example = "사용 X 서버에서 추가함") private String authority;

    public User toEntity(){
        Set<Authority> authorities = new HashSet<>();
        if(authority.equals(Authority.ADMIN.name())) authorities.add(Authority.ADMIN);
        authorities.add(Authority.USER);

        return User.builder()
                .password(password)
                .name(name)
                .phone(phone)
                .department(department)
                .authorities(authorities)
                .build();
    }
}
