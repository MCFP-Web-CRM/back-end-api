package com.mcfuturepartners.crm.api.product.service;


import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.dto.ProductRevenueDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.dto.ProductRevenueResponseDto;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<ProductRevenueResponseDto> findAllProductRevenue() {
        List<Product> productList = productRepository.findAll();
        LocalDateTime startOfMonth = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1).atStartOfDay();
        LocalDateTime startOfDay = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth()).atStartOfDay();

        List<ProductRevenueResponseDto> productMonthlyDailyRevenueList = new ArrayList<>();

        productMonthlyDailyRevenueList.add(ProductRevenueResponseDto.builder().unit("monthlyRevenue").productRevenueList(
                productList.stream().map(product ->
                        ProductRevenueDto.builder()
                                .product(modelMapper.map(product, ProductDto.class))
                                .amount(product.getTotalRevenueAfter(startOfMonth))
                                .build()
                ).sorted((o1, o2) -> (int) (o2.getAmount() - o1.getAmount())).collect(Collectors.toList())).build());

        productMonthlyDailyRevenueList.add(ProductRevenueResponseDto.builder().unit("dailyRevenue").productRevenueList(
                productList.stream().map(product ->
                        ProductRevenueDto.builder()
                                .product(modelMapper.map(product,ProductDto.class))
                                .amount(product.getTotalRevenueAfter(startOfDay))
                                .build()
                ).sorted((o1, o2) -> (int) (o2.getAmount() - o1.getAmount())).collect(Collectors.toList())).build());

        return productMonthlyDailyRevenueList;
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
