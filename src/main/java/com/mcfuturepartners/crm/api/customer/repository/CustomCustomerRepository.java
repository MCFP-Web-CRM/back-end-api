package com.mcfuturepartners.crm.api.customer.repository;

import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomCustomerRepository {
    Page<Customer> search(CustomerSearch customerSearch, Pageable pageable);
}
