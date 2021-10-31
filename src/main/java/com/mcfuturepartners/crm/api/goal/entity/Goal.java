package com.mcfuturepartners.crm.api.goal.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "goal")
@Entity
public class Goal {
    @Id
    @Column(name = "goal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(name = "goal_year")
    private Integer year;

    @Column(name = "goal_month")
    private Integer month;

    @Column(name = "goal_revenue_amount")
    private Long revenueAmount;

}
