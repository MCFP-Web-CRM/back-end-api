package com.mcfuturepartners.crm.api.user.dto;

import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.entity.ProductRevenue;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;

import java.time.LocalDate;

public class UserRevenueDto {

//service layer에서 처리 해야하지 않을까?
    public static UserRevenue salesRevenue(User user){
        return UserRevenue.builder()
                .name(user.getName())
                .dailySales(user.getOrders().stream()
                        .filter(order -> order
                                .getRegDate()
                                .isAfter(LocalDate.now().atStartOfDay()))
                        .map(order -> order.getProduct().getPrice())
                        .reduce(0, Integer::sum))
                .monthlySales(user.getOrders().stream()
                        .filter(order -> order
                                .getRegDate()
                                .isAfter(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth().getValue(),1).atStartOfDay()))
                        .map(order -> order.getProduct().getPrice())
                        .reduce(0, Integer::sum))
                .build();
    }
}
