package com.mcfuturepartners.crm.api.category.entity;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
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
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "category_name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Customer> customers = new ArrayList<>();

    public void removeConnectionWithCustomers(){
        if(customers.size() != 0){
            customers.stream().forEach(customer -> customer.setCategory(null));
        }
    }
}
