package com.mcfuturepartners.crm.api.counsel.repository;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CounselRepository extends JpaRepository<Counsel,String> {
    Optional<Counsel> findById(long id);
    Optional<Counsel> findByUserAndId(User user, long id);
    List<Counsel> findAllByCustomer(Customer customer);
    List<Counsel> findAllByUser(User user);
    List<Counsel> findAllByContentsContaining(String keyword);
    List<Counsel> findAllByUserAndContentsContaining(User user, String keyword);
    void deleteById(long id);
    boolean existsCounselByUserAndId(User user, long id);

}
