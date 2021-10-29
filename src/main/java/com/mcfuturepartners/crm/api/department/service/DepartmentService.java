package com.mcfuturepartners.crm.api.department.service;

import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Optional<Department> getDepartmentById(long departmentId);
    Department updateDepartment(DepartmentDto departmentDto);
    String deleteDepartment(long departmentId);
}
