package com.mcfuturepartners.crm.api.product.service;

import com.mcfuturepartners.crm.api.product.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductServiceImplTest {
    @Autowired
    private ProductService productService;

    @Test
    void save(){
        Product product = new Product();
        product.setName("product4");
        product.setPrice(3456);
        String save = productService.save(product);
        Assertions.assertThat(save).isEqualTo("successfully done");
    }
    @Test
    void saveFail_existData(){
        Product product = new Product();
        product.setName("product3");
        product.setPrice(3456);
        String save = productService.save(product);
        Assertions.assertThat(save).isEqualTo("product already exist");
    }
    @Test
    void find(){
        Product product = new Product();
        product.setName("product4");
        product.setPrice(3456);
        productService.save(product);
        Assertions.assertThat(productService.findProduct(product.getId()).getName()).isEqualTo("product4");
    }
    @Test
    void findAll(){
        Assertions.assertThat(productService.findAllProduct().size()).isEqualTo(3);
    }
    @Test
    void update(){
        Product product = productService.findProduct(1L);
        product.setName("product1234");
        Assertions.assertThat(productService.updateProduct(product)).isEqualTo("successfully done");
    }
    @Test
    void updateFail(){
        Product product = new Product();
        product.setId(1234L);
        product.setName("product1234");
        product.setPrice(1234);
        assertThrows(Exception.class,()->productService.updateProduct(product));
    }
    @Test
    void delete(){
        Assertions.assertThat(productService.deleteProduct(1L)).isEqualTo("successfully done");
    }
    @Test
    void deleteFail(){
        assertThrows(Exception.class,()->productService.deleteProduct(1234L));
    }
}