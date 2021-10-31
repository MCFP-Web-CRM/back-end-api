package com.mcfuturepartners.crm.api.revenue.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
public class Revenue {
    @Id
    @Column(name = "REVENUE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revenueId;
    private Integer year;
    private Integer month;
    private Long amount;
}
