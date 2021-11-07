package com.mcfuturepartners.crm.api.product.service;


import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        LocalDateTime startOfMonth = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonthValue(), 1).atStartOfDay();
        LocalDateTime startOfDay = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonthValue(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getDayOfMonth()).atStartOfDay();

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
    public ProductDto save(Product product) {
        try {
            Optional<Product> exist = productRepository.findByName(product.getName());
            if(exist.isPresent()){
                throw new FindException("Product already exist");
            }else{
                product = productRepository.save(product);
                return modelMapper.map(product, ProductDto.class);
            }
        } catch(Exception e){
            throw e;
        }
    }

    @Override
    public ProductDto updateProduct(Product product) {
        Product originalProduct = productRepository.findById(product.getId()).orElseThrow(()->new FindException("Prodcut "+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));


        try {
            originalProduct = productRepository.save(originalProduct.updateModified(product));
        } catch (Exception e){
            throw e;
        }
        return modelMapper.map(originalProduct,ProductDto.class);
    }

    @Override
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(()-> new FindException("Product "+ ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        product.removeConnectionWithOrders();

        try {
            productRepository.delete(product);
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }
}
