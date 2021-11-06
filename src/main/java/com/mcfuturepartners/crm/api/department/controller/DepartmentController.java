package com.mcfuturepartners.crm.api.department.controller;

import com.mcfuturepartners.crm.api.department.dto.DepartmentResponseDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.department.service.DepartmentService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
    private final DepartmentService departmentService;
    @GetMapping
    @ApiOperation(value = "부서 조회 api", notes = "부서 조회 api")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartment(){
        List<DepartmentResponseDto> departmentResponseDtoList;
        try{
            departmentResponseDtoList = departmentService.getAllDepartments();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(departmentResponseDtoList, HttpStatus.OK);
    }
}
