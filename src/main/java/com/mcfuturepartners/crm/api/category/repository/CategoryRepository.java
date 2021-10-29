package com.mcfuturepartners.crm.api.category.repository;

import com.mcfuturepartners.crm.api.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    Optional<Category> findById(long id);
}