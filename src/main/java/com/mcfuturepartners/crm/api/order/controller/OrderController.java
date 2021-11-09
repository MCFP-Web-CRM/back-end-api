package com.mcfuturepartners.crm.api.order.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.entity.OrderRevenue;
import com.mcfuturepartners.crm.api.order.service.OrderService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final TokenProvider tokenProvider;
/*

    @GetMapping("/customer/{customer-id}")
    @ApiOperation(value = "구독 조회 api", notes = "고객의 구독 전체 조회 api")
    public ResponseEntity<List<OrderResponseDto>> getOrdrers(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                             @PathVariable(name = "customer-id") Long customerId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

    }
*/

    @PostMapping
    @ApiOperation(value = "상품 구독 생성 api", notes = "상품 구독 생성 api")
    public ResponseEntity<List<OrderResponseDto>> createOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                              @RequestBody OrderDto orderDto){

        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        orderDto.setUsername(username);
        orderDto.setAuthorities(tokenProvider.getAuthentication(token).getAuthorities().toString());
        List<OrderResponseDto> orderResponseDtos;
        try{
            orderResponseDtos = orderService.saveOrder(orderDto);
        } catch (AuthorizationException authorizationException){

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(ObjectUtils.isEmpty(orderResponseDtos)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(orderResponseDtos,HttpStatus.CREATED);

    }
    @DeleteMapping(path = "/{order-id}")
    @ApiOperation(value = "상품 구독 취소 api", notes = "상품 구독 취소 api")
    public ResponseEntity<List<OrderResponseDto>> cancelOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                              @PathVariable(value="order-id") Long orderId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        List<OrderResponseDto> orderResponseDtos;

        try{
            orderResponseDtos = orderService.deleteOrder(OrderCancelDto.builder()
                    .authorities(tokenProvider.getAuthentication(token).getAuthorities().toString())
                    .username(username)
                    .orderId(orderId).build());
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(orderResponseDtos, HttpStatus.NO_CONTENT);
    }

}
