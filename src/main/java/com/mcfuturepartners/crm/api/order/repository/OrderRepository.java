package com.mcfuturepartners.crm.api.order.repository;

import com.mcfuturepartners.crm.api.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
}
