package com.mcfuturepartners.crm.api.admin.department;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.service.CategoryService;
import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.dto.DepartmentResponseDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.department.service.DepartmentService;
import com.mcfuturepartners.crm.api.exception.FindException;
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
@RequestMapping(value = "/admin/department")
@RequiredArgsConstructor
public class AdminController {
    private final DepartmentService departmentService;
    private final CategoryService categoryService;


    @PostMapping
    @ApiOperation(value = "부서 생성", notes = "회사 부서 생성한다.")
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDto departmentDto){
        try{
            departmentService.addDeparment(departmentDto);
        }catch(Exception e){
            return new ResponseEntity<String>("creation failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("department added", HttpStatus.CREATED);
    }

    @PutMapping(value = "/{department-id}")
    @ApiOperation(value = "부서명 수정", notes = "부서명 수정(추가 구현 필요)")
    @ApiImplicitParam(name = "departmentId", value = "부서 ID", required = true)
    public ResponseEntity<String> updateDepartment(@PathVariable(name = "department-id") Long departmentId,
                                                   @RequestBody DepartmentDto departmentDto){
        try{
            departmentService.updateDepartment(departmentId, departmentDto);
        }catch(FindException findException) {
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("department modified", HttpStatus.OK);
    }

    @DeleteMapping(value = "/{department-id}")
    @ApiOperation(value = "부서 삭제", notes = "부서 삭제")
    @ApiImplicitParam(name = "departmentId", value = "부서 ID", required = true)
    public ResponseEntity<String> deleteDepartment(@PathVariable(name = "department-id") Long departmentId){
        try{
            departmentService.deleteDepartment(departmentId);
        }catch(FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
