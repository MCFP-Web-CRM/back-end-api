package com.mcfuturepartners.crm.api.department.service;

import com.mcfuturepartners.crm.api.department.dto.DepartmentDto;
import com.mcfuturepartners.crm.api.department.dto.DepartmentResponseDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<DepartmentResponseDto> addDeparment(DepartmentDto departmentDto) {
        departmentRepository.save(departmentDto.toEntity());
        return departmentRepository.findAll().stream().map(department -> modelMapper.map(department,DepartmentResponseDto.class)).collect(Collectors.toList());
    }
    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(department -> modelMapper.map(department,DepartmentResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<DepartmentResponseDto> getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).map(department -> modelMapper.map(department,DepartmentResponseDto.class));
    }

    @Override
    public List<DepartmentResponseDto> updateDepartment(Long departmentId, DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        department.setName(departmentDto.getDepartmentName());

        return getAllDepartments();
    }

    @Override
    public List<DepartmentResponseDto> deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new FindException("Department "+ ErrorCode.RESOURCE_NOT_FOUND));
        department.removeConnectionWithUser();

        departmentRepository.deleteById(departmentId);
        return getAllDepartments();
    }
}
