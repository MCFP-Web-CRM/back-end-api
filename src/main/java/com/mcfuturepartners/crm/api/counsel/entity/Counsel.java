package com.mcfuturepartners.crm.api.counsel.entity;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "counsels")
@AllArgsConstructor
@NoArgsConstructor
public class Counsel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    //ManytoOne
    @ManyToOne
    @JoinColumn(name = "custumer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String product;

    //진행상황
    private String status;
    private String contents;

    private Date regDate;
}
