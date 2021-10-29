package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.admin.controller.AdminController;
import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerServiceImplTest {
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AdminController adminController;


    @Test
    void save(){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("영업1팀");
        adminController.createDepartment(departmentDto);


        UserDto userDto = new UserDto();
        userDto.setDepartmentId(1);
        userDto.setAuthority("ADMIN");
        userDto.setUsername("rootUser");
        userDto.setPassword("123123");
        userDto.setPhone("01095510270");
        userDto.setName("박재현");

        userService.signup(userDto);

        CustomerRegisterDto customerDto = new CustomerRegisterDto();
        customerDto.setName("이남순");
        customerDto.setPhone("01095510270");
        customerDto.setEmail("test@test.com");
        customerDto.setFunnel("카카오톡");
        customerDto.setBirth("19920309");
        customerDto.setSex("남자");
        customerDto.setManagerUsername("rootUser");

        Assertions.assertThat(customerService.save(customerDto)).isEqualTo("successfully done");

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
        //customer.setManager(v+"");
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
        //customer.setManager(v+"");
        customer.setRegDate(LocalDateTime.now());
        customer.setFunnel("카카오톡");
        String saveResult = customerService.deleteCustomer(customer.getId());
        Assertions.assertThat(saveResult).isEqualTo("successfully done");
    }
}