package com.mcfuturepartners.crm.api;

import com.mcfuturepartners.crm.api.admin.controller.AdminController;
import com.mcfuturepartners.crm.api.admin.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.controller.DepartmentController;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.user.controller.UserController;
import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.mcfuturepartners.crm.api.user.service.UserService;
import com.mcfuturepartners.crm.api.user.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userService;
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
}
