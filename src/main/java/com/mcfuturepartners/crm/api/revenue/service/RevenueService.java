package com.mcfuturepartners.crm.api.revenue.service;

import com.mcfuturepartners.crm.api.revenue.dto.ProductMonthlyRequest;
import com.mcfuturepartners.crm.api.revenue.dto.ProductMonthlyRevenue;
import com.mcfuturepartners.crm.api.revenue.dto.UserMonthlyRequest;
import com.mcfuturepartners.crm.api.revenue.dto.UserMonthlyRevenue;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;

import java.util.List;

public interface RevenueService {
    List<Revenue> getCompanyLatestRevenueByMonth(Integer month);
    List<List<UserMonthlyRevenue>> getUsersLatestRevenueByMonth(UserMonthlyRequest userMonthlyRequest);
    List<List<ProductMonthlyRevenue>> getProductsLatestRevenueByMonth(ProductMonthlyRequest productMonthlyRequest);
}
