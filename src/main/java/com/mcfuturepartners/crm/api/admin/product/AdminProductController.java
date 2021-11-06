package com.mcfuturepartners.crm.api.admin.product;

import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.product.controller.ProductController;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.service.ProductService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.regexp.RE;
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
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto){

        try{
            productDto = productService.save(productDto.toEntity());
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }
    @PutMapping(path = "/{product-id}")
    @ApiOperation(value = "상품 수정 api", notes = "상품명, 가격 변경")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(value = "product-id") long productId,
                                                 @RequestBody ProductDto productDto){

        productDto.setId(productId);
        try{
            productDto = productService.updateProduct(productDto.toEntity());
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @DeleteMapping(path = "/{product-id}")
    @ApiOperation(value = "상품 수정 api", notes = "상품 삭제 변경")
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "product-id") long productId){

        try{
            productService.deleteProduct(productId);
        }catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("successfully done",HttpStatus.OK);

    }
}
