package com.mcfuturepartners.crm.api.department.controller;

import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentRepository departmentRepository;
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartment(){
        return new ResponseEntity<>(departmentRepository.findAll(), HttpStatus.OK);
    }
}
