package com.mcfuturepartners.crm.api.department.service;

import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getDepartmentById(long departmentId) {
        return departmentRepository.findById(departmentId);
    }

    @Override
    public Department updateDepartment(DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentDto.getId()).orElseThrow();
        department.setName(departmentDto.getDepartmentName());

        return departmentRepository.save(department);
    }

    @Override
    public String deleteDepartment(long departmentId) {
        departmentRepository.deleteById(departmentId);
        return "delete successful";
    }
}
