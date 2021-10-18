package com.mcfuturepartners.crm.api.customer.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    List<Customer> findAll();
//    Customer getByUsername(String username);
//    Customer save(Customer entity);
//    Optional<Customer> findByUsername(String username);
//    boolean existsByUsername(String username);

    //create
    Customer save(Customer customer);
    //research
    Customer findByNo(String customerNo);
    List<Customer> findByManager(String managerNo);
    List<Customer> findByRegdate(String regDate);
    List<Customer> findByProduct(String Product);
    List<Customer> findByFunnel(String Funnel);
    List<Customer> findByPhone(String Funnel);



//    //update
//    boolean updateCustomer(Customer customer);
//
//    //delete
//    boolean deleteCustomer(String customerno);
}
