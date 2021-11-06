package com.mcfuturepartners.crm.api.customer.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.funnel.entity.Funnel;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "regdate")
    @NotNull
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funnel_id")
    private Funnel funnel;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Counsel> counsels = new ArrayList<>();
    public void setCategory(Category category){
        if(category == null){
            this.category = null;
            return;
        }
        if(this.category != null){
            this.category.getCustomers().remove(this);
        }
        this.category = category;
        if(!category.getCustomers().contains(this)){
            category.addCustomer(this);
        }
    }
    public void setFunnel(Funnel funnel){
        if(funnel == null) {
            this.funnel = null;
            return;
        }

        if(this.funnel != null){
            this.funnel.getCustomers().remove(this);
        }
        this.funnel = funnel;

        if(!funnel.getCustomers().contains(this)){
            funnel.addCustomer(this);
        }
    }
    public void removeOrdersFromCustomer(){
        if(orders.size() != 0){
            orders.stream().forEach(order -> order.setCustomer(null));
        }
    }
    public Customer updateModified(CustomerUpdateDto customerUpdateDto){
        if(StringUtils.hasText(customerUpdateDto.getPhone())){
            this.phone = customerUpdateDto.getBirth();
        }
        if(StringUtils.hasText(customerUpdateDto.getName())){
            this.name = customerUpdateDto.getName();
        }
        if(StringUtils.hasText(customerUpdateDto.getBirth())){
            this.birth = customerUpdateDto.getBirth();
        }
        if(StringUtils.hasText(customerUpdateDto.getEmail())){
            this.email = customerUpdateDto.getEmail();
        }
        if(StringUtils.hasText(customerUpdateDto.getSex())){
            this.sex = customerUpdateDto.getSex();
        }
        return this;
    }


}
