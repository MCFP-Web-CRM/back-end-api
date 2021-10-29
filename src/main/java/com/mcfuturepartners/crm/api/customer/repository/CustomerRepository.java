package com.mcfuturepartners.crm.api.customer.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    //create
    Customer save(Customer customer);
    //research
    Optional<Customer> findById(Long id);
    Optional<Customer>findByNameAndPhone(String name, String phone);
    List<Customer> findByManager(String managerNo);
    List<Customer> findByRegDate(String regDate);
//    List<Customer> findByProduct(String Product);
    List<Customer> findByFunnel(String Funnel);
    List<Customer> findByPhone(String Funnel);

}
