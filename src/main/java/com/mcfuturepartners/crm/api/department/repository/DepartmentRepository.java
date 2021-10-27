package com.mcfuturepartners.crm.api.department.repository;

import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    Optional<Department> findById(long id);

}
