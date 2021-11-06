package com.mcfuturepartners.crm.api.user.dto;

import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto {
    @ApiModelProperty(position = 0, example = "사원 등록 시 필요없음") private Long id;
    @ApiModelProperty(position = 1, example = "사원 계정 id(username)") private String username;
    @ApiModelProperty(position = 2, example = "사원 계정 비밀번호") private String password;
    @ApiModelProperty(position = 3, example = "사원 명") private String name;
    @ApiModelProperty(position = 4, example = "핸드폰 번호") private String phone;
    @ApiModelProperty(position = 5, example = "부서 ") private Long departmentId;
    @ApiModelProperty(position = 6, example = "권한 / 사용자 추가 시 필요! USER, ADMIN 둘 중 하나") private String authority;
    @ApiModelProperty(position = 6, example = "부서 / 사용자 추가 시 필요 없음")private Department department;

    public User toEntity(){
        Set<Authority> authorities = new HashSet<>();
        if(authority.equals(Authority.ADMIN.name())) authorities.add(Authority.ADMIN);
        authorities.add(Authority.USER);

        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .phone(phone)
                .department(department)
                .authorities(authorities)
                .build();
    }
}