package com.mcfuturepartners.crm.api;

import com.mcfuturepartners.crm.api.admin.controller.AdminController;
import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.service.CustomerServiceImpl;
import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.controller.DepartmentController;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.user.controller.UserController;
import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.mcfuturepartners.crm.api.user.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private DepartmentController departmentController;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AdminController adminController;

    @BeforeEach
    public void setUp(){

    }

    @Test @DisplayName("User Department Allocation Test")
    public void userDepartmentAllocationTest(){

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("영업1팀");
        adminController.createDepartment(departmentDto);

        assertThat(departmentRepository.findById(1).get().getName(),is(equalTo("영업1팀")));

        UserDto userDto = new UserDto();
        userDto.setDepartmentId(1);
        userDto.setAuthority("ADMIN");
        userDto.setUsername("rootUser");
        userDto.setPassword("123123");
        userDto.setPhone("01095510270");
        userDto.setName("박재현");

        userService.signup(userDto);

        assertThat(userRepository.getByUsername("rootUser").getDepartment().getName(),is(equalTo("영업1팀")));
    }
    @Transactional
    @Test @DisplayName("User Customer Add Test")
    public void userCustomerAllocationTest(){

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

        Assertions.assertThat(userService.getUserById(1).getCustomers().size()).isEqualTo(1);
    }
}
