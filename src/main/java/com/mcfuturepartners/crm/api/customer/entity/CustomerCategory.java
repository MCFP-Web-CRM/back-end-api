package com.mcfuturepartners.crm.api.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "customer_category")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCategory {
    @Id
    @Column(name = "customer_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "customer_category_name")
    private String name;
/*
    @OneToMany(mappedBy = "customer-category")
    private List<Customer> customers = new ArrayList<>();*/
}
