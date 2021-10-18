package com.mcfuturepartners.crm.api.customer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "phone")
    @NotNull
    private String phone;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "birth")
    @NotNull
    private String birth;

    @Column(name = "sex")
    @NotNull
    private String sex;

    @Column(name = "manager")
    @NotNull
    private String manager;

    @Column(name = "regdate")
    @NotNull
    private Date regDate;

    @Column(name = "funnel")
    private String funnel;

    @Column(name = "special_note")
    private String specialNote;

    @Column(name = "business_status")
    private String businessStatus;
}
