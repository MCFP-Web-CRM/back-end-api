package com.mcfuturepartners.crm.api.admin.product;

import com.mcfuturepartners.crm.api.product.controller.ProductController;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.service.ProductService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/product")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;
    private final TokenProvider tokenProvider;
    @PostMapping
    @ApiOperation(value = "상품 생성 api", notes = "구독 진행 가능 상품 추가 ")
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
    @PutMapping(value = "/{product-id}")
    @ApiOperation(value = "상품 수정 api", notes = "상품명, 가격 변경")
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
    @ApiOperation(value = "상품 수정 api", notes = "상품 삭제 변경")
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
