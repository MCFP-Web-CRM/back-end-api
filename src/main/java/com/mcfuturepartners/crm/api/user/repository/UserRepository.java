package com.mcfuturepartners.crm.api.user.repository;

import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAll();
    User getByUsername(String username);
    User save(User entity);
    Optional<User> findByUsername(String username);
    boolean existsById(long id);
    boolean existsByUsername(String username);
    void deleteById(long id);

}
