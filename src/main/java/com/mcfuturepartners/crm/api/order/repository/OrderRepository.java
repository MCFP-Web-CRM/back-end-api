package com.mcfuturepartners.crm.api.order.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.user.entity.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {
    Optional<Order> findById(long id);
    List<Order> findAllByCustomer(Customer customer);
    List<Order> findAllByUser(User user);
    void deleteById(long id);
    List<Order> findAllByRegDateIsAfter(LocalDateTime localDateTime);
    List<Order> findAllByRegDateIsBetween(LocalDateTime startDate, LocalDateTime endDate);
}
