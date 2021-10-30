package com.mcfuturepartners.crm.api.department.service;

import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.dto.DepartmentResponseDto;
import com.mcfuturepartners.crm.api.department.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
     List<DepartmentResponseDto> addDeparment(DepartmentDto departmentDto) ;

     List<DepartmentResponseDto> getAllDepartments();
    Optional<DepartmentResponseDto> getDepartmentById(Long departmentId);
    List<DepartmentResponseDto> updateDepartment(Long departmentId, DepartmentDto departmentDto);
    List<DepartmentResponseDto> deleteDepartment(Long departmentId);
}
