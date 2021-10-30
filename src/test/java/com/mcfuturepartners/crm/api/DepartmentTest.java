package com.mcfuturepartners.crm.api;

import com.mcfuturepartners.crm.api.admin.department.AdminController;
import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.controller.DepartmentController;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.department.service.DepartmentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@TestPropertySource("classpath:application-test.yml")
@AutoConfigureMockMvc
public class DepartmentTest {
    @Autowired
    private DepartmentServiceImpl departmentService;
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

    @Test
    @DisplayName("Department Update Test")
    public void departmentUpdateTest(){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("영업1팀");
        adminController.createDepartment(departmentDto);
        assertThat(departmentRepository.findById(1).get().getName(), is(equalTo("영업1팀")));

        DepartmentDto newDepartmentDto = new DepartmentDto();
        newDepartmentDto.setId(departmentRepository.findById(1).get().getId());
        newDepartmentDto.setDepartmentName("기획1팀");
        departmentService.updateDepartment(newDepartmentDto);

        assertThat(departmentRepository.findById(1).get().getName(), is(equalTo("기획1팀")));
    }

    @Test
    @DisplayName("Department Delete Test")
    public void departmentDeleteTest(){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("영업1팀");
        adminController.createDepartment(departmentDto);

        departmentService.deleteDepartment(1);
        assertThat(departmentRepository.findById(1).stream().count(),is(equalTo(0L)));
    }
    @Test
    @DisplayName("Department Get Test")
    public void departmentGetTest(){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("운영진");
        adminController.createDepartment(departmentDto);

        DepartmentDto departmentDto1 = new DepartmentDto();
        departmentDto.setDepartmentName("영업1팀");
        adminController.createDepartment(departmentDto1);

        DepartmentDto departmentDto2 = new DepartmentDto();

    }
}

