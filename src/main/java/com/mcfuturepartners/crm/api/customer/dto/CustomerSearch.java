package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.user.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerSearch {
    private String customerName;
    private String customerPhone;
    private String categoryName;
    private String productName;
    //고객 상태 바탕으로 검색
    private String counselStatus;
    private Long funnelId;
    private Long userId;
    private List<Long> managerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String counselKeyword;
    private String authority;
    private String username;
    private Boolean monthSalesCustomer;
    private Boolean monthRefundCustomer;
}
