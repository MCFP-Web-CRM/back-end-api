package com.mcfuturepartners.crm.api.user.repository;

import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<User, String> {
}