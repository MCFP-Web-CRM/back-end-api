package com.mcfuturepartners.crm.api.customer.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {

    //create
    Customer save(Customer customer);
    //research
    Optional<Customer> findById(Long id);
    Optional<Customer>findByNameAndPhone(String name, String phone);
    List<Customer> findByManager(String managerNo);
    List<Customer> findByRegDateIsAfter(LocalDateTime startDate);
    long countCustomerByRegDateIsAfter(LocalDateTime startDate);
    Optional<Customer> findFirstByPhone(String phone);

    Optional<Customer> findByPhone(String phone);
//    List<Customer> findByProduct(String Product);
    List<Customer> findByFunnel(String funnel);
    List<Customer> findTopByPhone(String phone);

}
