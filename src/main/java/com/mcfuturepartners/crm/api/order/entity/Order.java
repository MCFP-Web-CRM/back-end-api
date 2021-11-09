package com.mcfuturepartners.crm.api.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.exception.RefundException;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.refund.entity.Refund;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "price")
    @NotNull
    private Long price;

    @Column(name = "investment_amount")
    private Long investmentAmount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "regdate")
    @NotNull
    private LocalDateTime regDate;

    @OneToOne
    @JoinColumn(name = "refund_id")
    private Refund refund;

    public Order setRefund(Refund refund){
        if(this.refund!=null){
            this.refund = refund;
        }
        if(this.price - refund.getRefundAmount() < 0){
            throw new RefundException("Refund 가능한 금액이 아닙니다.");
        }
        return this;
    }
}
