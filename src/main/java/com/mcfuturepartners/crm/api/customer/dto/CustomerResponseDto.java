package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.funnel.dto.FunnelResponseDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
@Data
@Component
@NoArgsConstructor
public class CustomerResponseDto {

    @ApiModelProperty(position = 0, example = "고객 등록 번호")
    private Long id;
    @ApiModelProperty(position = 1, example = "고객 명")
    private String name;
    @ApiModelProperty(position = 2, example = "고객 생년 월일")
    private String birth;
    @ApiModelProperty(position = 3, example = "고객 email")
    private String email;
    @ApiModelProperty(position = 4, example = "고객 휴대폰 번호")
    private String phone;
    @ApiModelProperty(position = 5, example = "성별")
    private String sex;
    @ApiModelProperty(position = 6, example = "유입 경로")
    private FunnelResponseDto funnel;
    @ApiModelProperty(position = 7, example = "고객 상태 그룹")
    private CategoryDto category;
    @ApiModelProperty(position = 8, example = "담당 직원")
    private UserResponseDto manager;
    @ApiModelProperty(position = 8, example = "구독 상품 이력")
    private List<OrderResponseDto> orderList;
    @ApiModelProperty(position = 8, example = "상담 이력")
    private List<CounselDto> counselList;
}
