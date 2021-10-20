package com.mcfuturepartners.crm.api.product.service;

import com.mcfuturepartners.crm.api.product.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<Product> findAllProduct();
    Product findProduct(Long id);
    String save(Product product);
    List<Product> selectProduct(Map<String,String> map);
    String updateProduct(Product product);
    String deleteProduct(Long id);
}
