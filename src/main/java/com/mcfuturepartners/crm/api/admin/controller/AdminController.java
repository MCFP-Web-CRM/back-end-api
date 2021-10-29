package com.mcfuturepartners.crm.api.admin.controller;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.service.CategoryService;
import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {
    private final DepartmentRepository departmentRepository;
    private final CategoryService categoryService;
    @PostMapping(value = "/department")
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDto departmentDto){
        try{
            departmentRepository.save(departmentDto.toEntity());
        }catch(Exception e){
            return new ResponseEntity<String>("creation failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("department added", HttpStatus.CREATED);
    }
    @GetMapping(value = "/department")
    public ResponseEntity<List<Department>> getDepartments(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping(value = "/department/{department-id}")
    public ResponseEntity<String> updateDepartment(@PathVariable long departmentId){
     return new ResponseEntity<String>("department modified", HttpStatus.OK);
    }

    @DeleteMapping(value = "/department/{department-id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long departmentId){
        return new ResponseEntity<>("department deleted", HttpStatus.NO_CONTENT);
    }
    @PostMapping(value = "/category")
    public ResponseEntity<String> createCustomerCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }
}
