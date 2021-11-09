package com.mcfuturepartners.crm.api.order.service;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.entity.OrderRevenue;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import com.mcfuturepartners.crm.api.refund.dto.RefundDto;
import com.mcfuturepartners.crm.api.refund.dto.RefundResponseDto;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


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


        try{
            orderRepository.save(Order.builder()
                    .customer(customerRepository.findById(orderDto.getCustomerId()).get())
                    .user(userRepository.findByUsername(orderDto.getUsername()).get())
                    .product(product)
                            .price(orderDto.getPrice())
                            .investmentAmount(orderDto.getInvestmentAmount())
                            .regDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime())
                    .build());

            return orderRepository.findAllByCustomer(customer).stream().filter(order -> !ObjectUtils.isEmpty(order.getProduct())).map(order->{
                OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
                if(!ObjectUtils.isEmpty(order.getProduct())){
                orderResponseDto.setProduct(modelMapper.map(order.getProduct(), ProductDto.class));
                }
                if(!ObjectUtils.isEmpty(order.getRefund())){
                    orderResponseDto.setRefundDto(modelMapper.map(order.getRefund(), RefundResponseDto.class));
                }
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
            return orderRepository.findAllByCustomer(customer).stream().filter(order -> !ObjectUtils.isEmpty(order.getProduct())).map(order->{
                OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
                if(!ObjectUtils.isEmpty(order.getProduct())){
                    orderResponseDto.setProduct(modelMapper.map(order.getProduct(), ProductDto.class));
                }
                if(!ObjectUtils.isEmpty(order.getRefund())){
                    orderResponseDto.setRefundDto(modelMapper.map(order.getRefund(), RefundResponseDto.class));
                }
                return orderResponseDto;
            }).collect(Collectors.toList());
        }catch(Exception e){
            throw e;
        }

    }



}
