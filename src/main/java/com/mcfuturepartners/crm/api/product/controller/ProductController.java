package com.mcfuturepartners.crm.api.product.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.entity.ProductRevenue;
import com.mcfuturepartners.crm.api.product.service.ProductService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    @ApiOperation(value = "상품별 매출 구하기", notes = "현재 1달치만 나오고 추가적인 로직 구현중 / product 도메인에서 order 도메인으로 이전 할 예정이기 때문에 추후 url /order/product/revenue로 바뀔 듯")
    public ResponseEntity<List<ProductRevenue>> getProductsRevenue(){
        return new ResponseEntity<>(productService.findAllProductRevenue(),HttpStatus.OK);
    }

}
