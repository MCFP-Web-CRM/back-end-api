package com.mcfuturepartners.crm.api.order.service;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.entity.OrderRevenue;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<OrderResponseDto> saveOrder(OrderDto orderDto) {
        Customer customer = customerRepository.getById(orderDto.getCustomerId());
        Product product = productRepository.findById(orderDto.getProductId()).get();

        if(!orderDto.getAuthorities().contains("ADMIN")){
            if(!customer.getManager().getUsername().equals(orderDto.getUsername())){
                throw new AuthorizationException("Not the manager");
            }
        }

        if(customer.getOrders().stream().filter(order -> order.getProduct().equals(product)).count() != 0){
            return null;
        }

        try{
            orderRepository.save(Order.builder()
                    .customer(customerRepository.findById(orderDto.getCustomerId()).get())
                    .user(userRepository.findByUsername(orderDto.getUsername()).get())
                    .product(product)
                            .price(product.getPrice())
                            .regDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                    .build());

            return orderRepository.findAllByCustomer(customer).stream().map(order->{
                OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
                orderResponseDto.setProduct(modelMapper.map(order.getProduct(), ProductDto.class));
                return orderResponseDto;
            }).collect(Collectors.toList());
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<OrderResponseDto> deleteOrder(OrderCancelDto orderCancelDto)  {
        Customer customer = orderRepository.findById(orderCancelDto.getOrderId()).get().getCustomer();

        if(!orderCancelDto.getAuthorities().contains("ADMIN") && !orderRepository.findById(orderCancelDto.getOrderId()).get().getUser().getUsername().equals(orderCancelDto.getUsername())){
            return null;
        }
        try{
            orderRepository.deleteById(orderCancelDto.getOrderId());
            return orderRepository.findAllByCustomer(customer).stream().map(order->{
                OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
                orderResponseDto.setProduct(modelMapper.map(order.getProduct(), ProductDto.class));
                return orderResponseDto;
            }).collect(Collectors.toList());
        }catch(Exception e){
            throw e;
        }

    }

    @Override
    public OrderRevenue getTotalRevenue() {
        return OrderRevenue.builder().currentRevenue(
                orderRepository.findAllByRegDateIsAfter
                                (LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(),
                                                ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonthValue(),
                                        1)
                                        .atStartOfDay())
                        .stream()
                        .map(order -> order.getProduct().getPrice())
                        .reduce(0L,Long::sum))
                .build();
    }
}
