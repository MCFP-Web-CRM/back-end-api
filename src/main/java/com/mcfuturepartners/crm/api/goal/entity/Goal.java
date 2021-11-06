package com.mcfuturepartners.crm.api.goal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
@Data
@Table(name = "goal")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public Goal updateModified(Goal modifiedGoal){
        if(!ObjectUtils.isEmpty(modifiedGoal.getRevenueAmount())){
            this.setRevenueAmount(modifiedGoal.getRevenueAmount());
        }
        return this;
    }

}
