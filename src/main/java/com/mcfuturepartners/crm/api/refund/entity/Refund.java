package com.mcfuturepartners.crm.api.refund.entity;

import com.mcfuturepartners.crm.api.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@Table(name = "refunds")
@AllArgsConstructor
@NoArgsConstructor
public class Refund {
    @Id
    @Column(name = "refund_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refundId;

    @OneToOne
    private Order order;

    @Column(name = "refund_amount")
    private Long refundAmount;
}
