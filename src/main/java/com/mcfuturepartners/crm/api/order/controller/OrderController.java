package com.mcfuturepartners.crm.api.order.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.order.dto.OrderCancelDto;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.entity.OrderRevenue;
import com.mcfuturepartners.crm.api.order.service.OrderService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<List<OrderResponseDto>> createOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                              @RequestBody OrderDto orderDto){

        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        orderDto.setUsername(username);

        return new ResponseEntity<>(orderService.saveOrder(orderDto),HttpStatus.CREATED);

    }
    @DeleteMapping(path = "/{order-id}")
    public ResponseEntity<List<OrderResponseDto>> cancelOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                              @PathVariable(value="order-id") long orderId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();


            return new ResponseEntity<>(orderService.deleteOrder(OrderCancelDto.builder()
                            .authorities(tokenProvider.getAuthentication(token).getAuthorities().toString())
                            .username(username)
                            .orderId(orderId).build()), HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/revenue")
    public ResponseEntity<OrderRevenue> getCurrentRevenue(){
        return new ResponseEntity<>(orderService.getTotalRevenue(),HttpStatus.OK);
    }
}
