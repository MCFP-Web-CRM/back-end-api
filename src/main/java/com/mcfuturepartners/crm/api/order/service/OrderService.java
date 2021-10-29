package com.mcfuturepartners.crm.api.order.service;

import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.OrderRevenue;
import com.mcfuturepartners.crm.api.product.entity.Product;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> saveOrder(OrderDto orderDto);
    List<OrderResponseDto> deleteOrder(OrderCancelDto orderCancelDto);
    OrderRevenue getTotalRevenue();
}
