package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
@Component
@NoArgsConstructor
public class CustomerDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String name;
    @ApiModelProperty(position = 2) private String birth;
    @ApiModelProperty(position = 3) private String email;
    @ApiModelProperty(position = 4) private String phone;
    @ApiModelProperty(position = 5) private String sex;
    @ApiModelProperty(position = 6) private String funnel;
    private long categoryId;
    @Nullable
    private String specialNote;
    @Nullable
    private String managerUsername;
    @Nullable
    private long counselId;

    public Customer toEntity(){

            return Customer.builder()
                    .name(name)
                    .phone(phone)
                    .email(email)
                    .birth(birth)
                    .sex(sex)
                    .funnel(funnel)
                    .specialNote(specialNote)
                    //.businessStatus(businessStatus)
                    .regDate(LocalDateTime.now())
                    .build();
        }
}
