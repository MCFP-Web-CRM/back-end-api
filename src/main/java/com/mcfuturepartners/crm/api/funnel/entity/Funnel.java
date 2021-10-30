package com.mcfuturepartners.crm.api.funnel.entity;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "funnel")
@AllArgsConstructor
@NoArgsConstructor
public class Funnel {

    @Id
    @Column(name = "FUNNEL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long funnelId;

    @Column(name = "FUNNEL_NAME")
    private String funnelName;

    @OneToMany(mappedBy = "funnel")
    private List<Customer> customers;
}
