package com.mcfuturepartners.crm.api.product.controller;

import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.dto.ProductRevenueResponseDto;
import com.mcfuturepartners.crm.api.product.service.ProductService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final TokenProvider tokenProvider;


    @GetMapping
    @ApiOperation(value = "현재 구독 가능한 상품", notes = "현재 구독가능한 상품들 모두 호출")
    public ResponseEntity<List<ProductDto>> getProducts(){
        return new ResponseEntity<>(productService.findAllProduct(),HttpStatus.OK);
    }

    @GetMapping("/revenue")
    @ApiOperation(value = "상품별 매출 조회 api", notes = "현재 이번달, 오늘 매출 전달")
    public ResponseEntity<List<ProductRevenueResponseDto>> getProductsRevenue(){
        return new ResponseEntity<>(productService.findAllProductRevenue(),HttpStatus.OK);
    }

}
