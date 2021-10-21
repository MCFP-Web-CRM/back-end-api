package com.mcfuturepartners.crm.api.product.dto;

import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.entity.ProductRevenue;

import java.time.LocalDate;
import java.util.List;

public class ProductRevenueDto{

    public static ProductRevenue salesRevenue(Product product){
        return ProductRevenue.builder()
                .productName(product.getName())
                .dailySales(product.getOrders().stream()
                        .filter(order -> order
                                .getRegDate()
                                .isAfter(LocalDate.now().atStartOfDay()))
                        .map(order -> order.getProduct().getPrice())
                        .reduce(0, Integer::sum))
                .monthlySales(product.getOrders().stream()
                        .filter(order -> order
                                .getRegDate()
                                .isAfter(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth().getValue(),1).atStartOfDay()))
                        .map(order -> order.getProduct().getPrice())
                        .reduce(0, Integer::sum))
                .build();
    }
}
