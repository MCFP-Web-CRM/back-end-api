package com.mcfuturepartners.crm.api.product.service;

import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.dto.ProductRevenueResponseDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDto> findAllProduct();
    Product findProduct(Long id);
    List<ProductRevenueResponseDto> findAllProductRevenue();
    ProductDto save(Product product);
    ProductDto updateProduct(Product product);
    String deleteProduct(Long id);
}
