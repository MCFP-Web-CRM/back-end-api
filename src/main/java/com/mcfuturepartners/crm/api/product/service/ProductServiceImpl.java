package com.mcfuturepartners.crm.api.product.service;


import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.dto.ProductRevenueDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.entity.ProductRevenue;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<ProductDto> findAllProduct() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,"doesn't exist product"));
    }

    @Override
    public List<ProductRevenue> findAllProductRevenue() {
        return productRepository.findAll().stream().map(product -> ProductRevenueDto.salesRevenue(product)).collect(Collectors.toList());
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
