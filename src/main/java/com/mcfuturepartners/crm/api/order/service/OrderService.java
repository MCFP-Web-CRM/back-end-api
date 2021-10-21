package com.mcfuturepartners.crm.api.order.service;

import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.product.entity.Product;

public interface OrderService {
    String saveOrder(OrderDto orderDto);
    String deletOrder(OrderCancelDto orderCancelDto);
}
