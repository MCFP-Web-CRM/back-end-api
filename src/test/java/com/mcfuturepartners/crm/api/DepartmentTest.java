package com.mcfuturepartners.crm.api;

import com.mcfuturepartners.crm.api.admin.controller.AdminController;
import com.mcfuturepartners.crm.api.admin.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.controller.DepartmentController;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.user.controller.UserController;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.mcfuturepartners.crm.api.user.service.UserServiceImpl;
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
public class DepartmentTest {
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

    @Test
    @DisplayName("Department Create Test")
    public void departmentCreateTest() {

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("영업1팀");
        adminController.createDepartment(departmentDto);

        assertThat(departmentRepository.findById(1).get().getName(), is(equalTo("영업1팀")));
    }
}
