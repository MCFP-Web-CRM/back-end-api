package com.mcfuturepartners.crm.api.product.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.entity.ProductRevenue;
import com.mcfuturepartners.crm.api.product.service.ProductService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
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
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                             @RequestBody ProductDto productDto){
        String token = bearerToken.replace("Bearer ", "");

        if(!tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(productService.save(productDto.toEntity()).equals("successfully done")){
            return new ResponseEntity<>("successfully done", HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts(){
        return new ResponseEntity<>(productService.findAllProduct(),HttpStatus.OK);
    }
    @GetMapping("/revenue")
    public ResponseEntity<List<ProductRevenue>> getProductsRevenue(){
        return new ResponseEntity<>(productService.findAllProductRevenue(),HttpStatus.OK);
    }

    @PutMapping(value = "/{product-id}")
    public ResponseEntity<String> updateProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                @PathVariable(value = "product-id") long productId,
                                                @RequestBody ProductDto productDto){
        String token = bearerToken.replace("Bearer ", "");

        if(!tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        productDto.setId(productId);
        if(productService.updateProduct(productDto.toEntity()).equals("successfully done")){
            return new ResponseEntity<>("successfully done",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{product-id}")
    public ResponseEntity<String> deleteProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                @PathVariable(value = "product-id") long productId){
        String token = bearerToken.replace("Bearer ", "");

        if(!tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(productService.deleteProduct(productId).equals("successfully done")){
            return new ResponseEntity<>("successfully done",HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
