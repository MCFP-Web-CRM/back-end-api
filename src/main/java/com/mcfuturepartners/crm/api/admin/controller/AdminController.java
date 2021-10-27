package com.mcfuturepartners.crm.api.admin.controller;

import com.mcfuturepartners.crm.api.admin.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final DepartmentRepository departmentRepository;

    @PostMapping(value = "/department")
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDto departmentDto){
        try{
            departmentRepository.save(departmentDto.toEntity());
        }catch(Exception e){
            return new ResponseEntity<String>("creation failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("department added", HttpStatus.CREATED);

    }
}
