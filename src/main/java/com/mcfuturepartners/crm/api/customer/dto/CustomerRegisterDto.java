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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@Component
@NoArgsConstructor
public class CustomerRegisterDto {
    @ApiModelProperty(position = 0, example = "고객 등록 번호, 고객 저장 시 필요 없음") private long id;
    @ApiModelProperty(position = 1, example = "고객 이름") private String name;
    @ApiModelProperty(position = 2, example = "고객 생년 월일") private String birth;
    @ApiModelProperty(position = 3, example = "고객 email") private String email;
    @ApiModelProperty(position = 4, example = "고객 phone") private String phone;
    @ApiModelProperty(position = 5, example = "고객 성별") private String sex;
    @ApiModelProperty(position = 6, example = "고객 유입 (필수 항목)") private Long funnelId;
    @ApiModelProperty(position = 6, example = "고객 상태 (필수 항목)")private Long categoryId;
    @ApiModelProperty(position = 6, example = "사원 명 (필요 없음)")private String managerUsername;

    public Customer toEntity(){

            return Customer.builder()
                    .name(name)
                    .phone(phone)
                    .email(email)
                    .birth(birth)
                    .sex(sex)
                    .regDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                    .build();
        }
}
