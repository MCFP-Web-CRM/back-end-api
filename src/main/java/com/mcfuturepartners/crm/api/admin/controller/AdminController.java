package com.mcfuturepartners.crm.api.admin.controller;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.service.CategoryService;
import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "부서 생성", notes = "회사 부서 생성한다.")
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDto departmentDto){
        try{
            departmentRepository.save(departmentDto.toEntity());
        }catch(Exception e){
            return new ResponseEntity<String>("creation failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("department added", HttpStatus.CREATED);
    }

    @GetMapping(value = "/department")
    @ApiOperation(value = "전체 부서 조회", notes = "전체 회사 부서 조회(현재 수정 작업 필요)")
    public ResponseEntity<List<Department>> getDepartments(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/department/{department-id}")
    @ApiOperation(value = "부서명 수정", notes = "부서명 수정(추가 구현 필요)")
    @ApiImplicitParam(name = "departmentId", value = "부서 ID", required = true)
    public ResponseEntity<String> updateDepartment(@PathVariable long departmentId){
     return new ResponseEntity<String>("department modified", HttpStatus.OK);
    }

    @DeleteMapping(value = "/department/{department-id}")
    @ApiOperation(value = "부서 삭제", notes = "부서 삭제(추가 구현 필요)")
    @ApiImplicitParam(name = "departmentId", value = "부서 ID", required = true)
    public ResponseEntity<String> deleteDepartment(@PathVariable long departmentId){
        return new ResponseEntity<>("department deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/category")
    @ApiOperation(value = "고객 그룹 추가", notes = "(예외 처리 필요)")
    public ResponseEntity<String> createCustomerCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }
}
