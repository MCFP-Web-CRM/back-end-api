package com.mcfuturepartners.crm.api.product.service;


import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProduct() {
        List<Product> productList = productRepository.findAll();
        return productList;
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,"doesn't exist product"));
    }

    @Override
    public String save(Product product) {
        try {
            Optional<Product> exist = productRepository.findByName(product.getName());
            if(exist.isPresent()){
                return "product already exist";
            }else{
                productRepository.save(product);
                return "successfully done";
            }
        } catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<Product> selectProduct(Map<String, String> map) {
        return null;
    }

    @Override
    public String updateProduct(Product product) {
        try {
            productRepository.save(productRepository.findById(product.getId()).get());
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public String deleteProduct(Long id) {
        try {
            productRepository.delete(productRepository.findById(id).get());
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }
}
