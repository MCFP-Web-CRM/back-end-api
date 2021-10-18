package com.mcfuturepartners.crm.api.customer.repository;


import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerRepositoryImplTest {
    @Autowired
    private CustomerRepository customerRepository;

//    CustomerRepositoryImplTest(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }

    @Test
    public void save(){
        Customer customer = new Customer();
        customer.setName("이남수");
        customer.setBirth("19960712");
        customer.setEmail("test@test.com");
        customer.setPhone("01012345678");
        customer.setSex("남");
        customer.setManager("2");
        customer.setRegDate(new Date());
        customer.setFunnel("내페이지");
        Customer save = customerRepository.save(customer);
//        customerRepository.registerCustomer(customer);
//        Customer find = customerRepository.findByCustomerNo(customer.getNo());
        Assertions.assertThat(save.getRegDate()).isEqualTo(customer.getRegDate());
        System.out.println("save.getRegDate() = " + save.getRegDate());
    }

    @Test
    public void update(){

    }

    @Test
    public void delete(){

    }

    @Test
    public void find(){
        Optional<Customer> find = customerRepository.findById(1l);
        Assertions.assertThat(find.get().getNo()).isEqualTo(1);
        System.out.println("find.get().toString() = " + find.get().toString());
    }

    @Test
    public void findAll(){
        List<Customer> customerList = customerRepository.findAll();
        Assertions.assertThat(customerList).isNotEmpty();
        System.out.println("customerList.toString() = " + customerList.toString());
    }
}