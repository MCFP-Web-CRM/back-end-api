package com.mcfuturepartners.crm.api.order.repository;

import com.mcfuturepartners.crm.api.order.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomeOrderRepository {
    List<Order> searchOrdersAfterAndBefore(LocalDateTime startDate, LocalDateTime endDate);
}
