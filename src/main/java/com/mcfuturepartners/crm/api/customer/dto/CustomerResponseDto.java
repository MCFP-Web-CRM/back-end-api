package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.Order;
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
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String name;
    @ApiModelProperty(position = 2) private String birth;
    @ApiModelProperty(position = 3) private String email;
    @ApiModelProperty(position = 4) private String phone;
    @ApiModelProperty(position = 5) private String sex;
    @ApiModelProperty(position = 6) private String funnel;
    //CategoryDto
    private CategoryDto category;
    @Nullable
    private String specialNote;
    //UserDto로 보내기
    private String managerName;
    private List<OrderResponseDto> orderList;
    private List<CounselDto> counselList;
}
