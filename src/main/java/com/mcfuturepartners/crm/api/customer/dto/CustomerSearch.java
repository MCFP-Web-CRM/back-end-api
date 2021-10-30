package com.mcfuturepartners.crm.api.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerSearch {
    private String categoryName;
    private String productName;
    private String funnel;
    private Long managerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String counselKeyword;
}
