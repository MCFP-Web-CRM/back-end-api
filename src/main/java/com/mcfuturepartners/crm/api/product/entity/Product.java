package com.mcfuturepartners.crm.api.product.entity;

import javax.persistence.Entity;

import com.mcfuturepartners.crm.api.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    @NotNull
    private String name;

    @Column(name = "product_price")
    @NotNull
    private Long price;

    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();

}
