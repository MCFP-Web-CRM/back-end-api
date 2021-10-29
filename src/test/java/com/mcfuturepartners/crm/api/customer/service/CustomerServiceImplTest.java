package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerServiceImplTest {
    @Autowired
    private CustomerService customerService;

    @Test
    void saveFail(){
        int v = (int) (Math.random() * 100 + 1);
        Customer customer = new Customer();
        customer.setName("이남수");
        customer.setBirth("19960712");
        customer.setEmail("test@test.com");
        customer.setPhone("01012345678");
        customer.setSex("남");
        customer.setManager(v+"");
        customer.setRegDate(LocalDateTime.now());
        customer.setFunnel("카카오톡");
//        Assertions.assertThrows(customerService.save(customer));
        assertThrows(Exception.class,()->customerService.save(customer));
    }
    @Test
    void save(){
        int v = (int) (Math.random() * 100 + 1);
        Customer customer = new Customer();
        customer.setName("이남순");
        customer.setBirth("19960712");
        customer.setEmail("test@test.com");
        customer.setPhone("01098765431");
        customer.setSex("남");
        customer.setManager(v+"");
        customer.setRegDate(LocalDateTime.now());
        customer.setFunnel("카카오톡");
        Assertions.assertThat(customerService.save(customer)).isEqualTo("successfully done");
    }
    @Test
    void find(){
        Assertions.assertThat(customerService.findCustomer(1L).getId()).isEqualTo(1);
    }

    @Test
    void findAll(){
        Assertions.assertThat(customerService.findAllCustomer().size()).isEqualTo(3);
    }

    @Test
    void update(){
        int v = (int) (Math.random() * 100 + 1);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("이남수");
        customer.setBirth("19960712");
        customer.setEmail("test@test.com");
        customer.setPhone("01012345678");
        customer.setSex("남");
        customer.setManager(v+"");
        customer.setRegDate(LocalDateTime.now());
        customer.setFunnel("카카오톡");
        String saveResult = customerService.updateCustomer(customer);
        Assertions.assertThat(saveResult).isEqualTo("successfully done");
    }

    @Test
    void delete(){
        int v = (int) (Math.random() * 100 + 1);
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("이남수");
        customer.setBirth("19960712");
        customer.setEmail("test@test.com");
        customer.setPhone("01012345678");
        customer.setSex("남");
        customer.setManager(v+"");
        customer.setRegDate(LocalDateTime.now());
        customer.setFunnel("카카오톡");
        String saveResult = customerService.deleteCustomer(customer.getId());
        Assertions.assertThat(saveResult).isEqualTo("successfully done");
    }
}