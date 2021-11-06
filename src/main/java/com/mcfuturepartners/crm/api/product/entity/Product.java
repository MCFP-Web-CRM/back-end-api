package com.mcfuturepartners.crm.api.product.entity;

import javax.persistence.Entity;

import com.mcfuturepartners.crm.api.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    public Product updateModified(Product modifiedProduct){
        if(StringUtils.hasText(modifiedProduct.getName())){
            this.name = modifiedProduct.getName();
        }
        if(!ObjectUtils.isEmpty(modifiedProduct.getPrice())){
            this.price = modifiedProduct.getPrice();
        }
        return this;
    }

    public void removeConnectionWithOrders(){
        if(orders.size() != 0 ){
            orders.stream().forEach(order -> order.setProduct(null));
        }
    }

    public Long getTotalRevenueAfter(LocalDateTime startTime) {
        return this.getOrders().stream()
                .filter(order -> order.getRegDate().isAfter(startTime))
                .map(order -> order.getPrice()).reduce(0L, Long::sum);
    }

}
