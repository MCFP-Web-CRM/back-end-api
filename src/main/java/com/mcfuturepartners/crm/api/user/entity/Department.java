package com.mcfuturepartners.crm.api.user.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Department {
    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "department_name")
    private String name;
}
