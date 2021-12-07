package com.mcfuturepartners.crm.api.user.repository;

import com.mcfuturepartners.crm.api.user.entity.User;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    User getByUsername(String username);
    User save(User entity);
    Optional<User> findByUsername(String username);
    Optional<User> findByName(String name);
    boolean existsByUsername(String username);

}
