package com.mcfuturepartners.crm.api.order.service;

import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.entity.OrderRevenue;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    @Override
    public String saveOrder(OrderDto orderDto) {
        log.info(orderDto.toString());

        try{
            orderRepository.save(Order.builder()
                    .customer(customerRepository.findById(orderDto.getCustomerId()).get())
                    .user(userRepository.findByUsername(orderDto.getUsername()).get())
                    .product(productRepository.findById(orderDto.getProductId()).get())
                            .regDate(LocalDateTime.now())
                    .build());
            return "successfully saved";
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public String deleteOrder(OrderCancelDto orderCancelDto) {

        if(!orderCancelDto.getAuthorities().contains("ADMIN") && !orderRepository.findById(orderCancelDto.getOrderId()).get().getUser().getUsername().equals(orderCancelDto.getUsername())){
            return ErrorCode.UNAUTHORIZED.getCode();
        }
        try{
            orderRepository.deleteById(orderCancelDto.getOrderId());
            return "successfully removed";
        }catch(Exception e){
            throw e;
        }

    }

    @Override
    public OrderRevenue getTotalRevenue() {
        return OrderRevenue.builder().currentRevenue(
                orderRepository.findAllByRegDateIsAfter
                                (LocalDate.of(LocalDateTime.now().getYear(),
                                        LocalDateTime.now().getMonthValue(),
                                        1)
                                        .atStartOfDay())
                        .stream()
                        .map(order -> order.getProduct().getPrice())
                        .reduce(0,Integer::sum))
                .build();
    }
}
