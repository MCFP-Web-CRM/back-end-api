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

@Data @Component
@NoArgsConstructor
public class UserDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String username;
    @ApiModelProperty(position = 2) private String password;
    @ApiModelProperty(position = 3) private String name;
    @ApiModelProperty(position = 4) private String phone;
    @ApiModelProperty(position = 5) private long departmentId;
    @ApiModelProperty(position = 6) private String authority;
    private Department department;

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