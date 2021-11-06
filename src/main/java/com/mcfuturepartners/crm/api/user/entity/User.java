package com.mcfuturepartners.crm.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.sms.entity.Sms;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Size(min = 20, max = 40, message = "8자리 이상 입력하시오")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id")
    private Department department;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Counsel> counsels = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<Sms> sms = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    public void setDepartment(Department department){
        if(this.department != null){
            this.department.getUsers().remove(this);
        }
        this.department = department;
        if(!department.getUsers().contains(this)){
            department.addUser(this);
        }
    }
    public void eraseConnectionFromUser(){
        removeUserFromCustomers();
        removeUserFromOrders();
        removeUserFromCounsels();
    }

    public void removeUserFromCounsels(){
        if(counsels.size() != 0){
            counsels.stream().forEach(counsel -> counsel.setUser(null));
        }
    }
    public void removeUserFromCustomers(){
        if(customers.size() != 0){
            customers.stream().forEach(customer -> customer.setManager(null));
        }
    }
    public void removeUserFromOrders(){
        if(orders.size() != 0){
            orders.stream().forEach(order -> order.setUser(null));
        }
    }
    public User updateModified(User modifiedUser){
        if(StringUtils.hasText(modifiedUser.getName())){
            this.setName(modifiedUser.getName());
        }
        if(StringUtils.hasText(modifiedUser.getPassword())){
            this.setPassword(modifiedUser.getPassword());
        }
        if(StringUtils.hasText(modifiedUser.getPhone())){
            this.setPhone(modifiedUser.getPhone());
        }
        if(!ObjectUtils.isEmpty(modifiedUser.getDepartment())){
            this.setDepartment(modifiedUser.getDepartment());
        }
        return this;
    }
    public Long getTotalRevenueAfter(LocalDateTime startTime){
        return this.getOrders().stream()
                .filter(order -> order.getRegDate().isAfter(startTime))
                .map(order -> order.getPrice()).reduce(0L,Long::sum);
    }
}
