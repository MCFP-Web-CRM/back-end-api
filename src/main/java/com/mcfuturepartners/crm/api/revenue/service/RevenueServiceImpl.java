package com.mcfuturepartners.crm.api.revenue.service;

import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import com.mcfuturepartners.crm.api.revenue.dto.ProductMonthlyRequest;
import com.mcfuturepartners.crm.api.revenue.dto.ProductMonthlyRevenue;
import com.mcfuturepartners.crm.api.revenue.dto.UserMonthlyRequest;
import com.mcfuturepartners.crm.api.revenue.dto.UserMonthlyRevenue;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

@Transactional
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<Revenue> getCompanyLatestRevenueByMonth(Integer month) {
        List<Revenue> revenueList = new ArrayList<>(month);

        LocalDateTime startDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate().getDayOfMonth()).atTime(23,59,59,99);

        for(int i = 0 ; i < month ; i ++){

            revenueList.add(Revenue.builder()
                    .year(startDate.getYear())
                    .month(startDate.getMonthValue())
                    .amount(orderRepository.findAllByRegDateIsBetween(startDate, endDate)
                    .stream().map(order ->{
                                if(!ObjectUtils.isEmpty(order.getRefund())){
                                    return order.getPrice()-order.getRefund().getRefundAmount();
                                }
                                return order.getPrice();
                            } ).reduce(0L,Long::sum)).build());

            startDate = startDate.minusMonths(1);
            endDate = endDate.minusMonths(1);
        }

        return revenueList;
    }

    @Override
    public List<List<UserMonthlyRevenue>> getUsersLatestRevenueByMonth(UserMonthlyRequest userMonthlyRequest) {
        List<User> userList = userRepository.findAllById(userMonthlyRequest.getUserIds());

        List<List<UserMonthlyRevenue>> userRevenues = new ArrayList<>();


        for(User user : userList){
            List<UserMonthlyRevenue> userMonthlyRevenue = new ArrayList<>();
            LocalDateTime startDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), 1).atStartOfDay();
            LocalDateTime endDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate().getDayOfMonth()).atTime(23,59,59,99);

            for(int period = 0 ; period < userMonthlyRequest.getMonth() ; period ++){

                userMonthlyRevenue.add(UserMonthlyRevenue.builder().user(modelMapper.map(user, UserResponseDto.class))
                        .year(startDate.getYear()).month(startDate.getMonthValue()).amount(orderRepository.findAllByRegDateIsBetween(startDate,endDate).stream()
                                .filter(order -> !ObjectUtils.isEmpty(order.getUser()))
                                .filter(order->order.getUser().equals(user))
                                .map(order ->{
                                    if(!ObjectUtils.isEmpty(order.getRefund())){
                                        return order.getPrice()-order.getRefund().getRefundAmount();
                                    }
                                    return order.getPrice();
                                } )
                                .reduce(0L,Long::sum)).build());

                startDate = startDate.minusMonths(1);
                endDate = endDate.minusMonths(1);
            }

            userRevenues.add(userMonthlyRevenue);
        }

        return userRevenues;
    }

    @Override
    public List<List<ProductMonthlyRevenue>> getProductsLatestRevenueByMonth(ProductMonthlyRequest productMonthlyRequest) {
        List<Product> productList = productRepository.findAllById(productMonthlyRequest.getProductIds());
        List<List<ProductMonthlyRevenue>> productRevenues = new ArrayList<>();

        for(Product product : productList){
            List<ProductMonthlyRevenue> productMonthlyRevenues = new ArrayList<>();

            LocalDateTime startDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), 1).atStartOfDay();
            LocalDateTime endDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate().getDayOfMonth()).atTime(23,59,59,99);

            for(int period = 0 ; period < productMonthlyRequest.getMonth() ; period ++){
                productMonthlyRevenues.add(ProductMonthlyRevenue.builder().product(modelMapper.map(product, ProductDto.class))
                        .year(startDate.getYear()).month(startDate.getMonthValue()).amount(orderRepository.findAllByRegDateIsBetween(startDate,endDate).stream()
                                .filter(order -> !ObjectUtils.isEmpty(order.getProduct()))
                                .filter(order-> order.getProduct().equals(product))
                                .map(order -> {
                                    if(!ObjectUtils.isEmpty(order.getRefund())){
                                        return order.getPrice()-order.getRefund().getRefundAmount();
                                    }
                                    return order.getPrice();
                                } )
                                .reduce(0L,Long::sum)).build());

                startDate = startDate.minusMonths(1);
                endDate = endDate.minusMonths(1);
            }
            productRevenues.add(productMonthlyRevenues);
        }

        return productRevenues;
    }

}
