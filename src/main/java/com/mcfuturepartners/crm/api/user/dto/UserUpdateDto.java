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
    @ApiModelProperty(position = 1, example = "username / 필수 X 서버에서 처리") private String username;
    @ApiModelProperty(position = 2, example = "사원 계정 비밀번호") private String password;
    @ApiModelProperty(position = 3, example = "사원 명") private String name;
    @ApiModelProperty(position = 4, example = "핸드폰 번호") private String phone;

    public User toEntity(){

        return User.builder()
                .password(password)
                .name(name)
                .phone(phone)
                .build();
    }
}
