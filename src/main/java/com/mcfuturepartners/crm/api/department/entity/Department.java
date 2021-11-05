package com.mcfuturepartners.crm.api.department.entity;

import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "department")
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "department_name")
    private String name;

    @OneToMany(mappedBy = "department")
    private List<User> users = new ArrayList<>();

    public void addUser(User user){
        this.users.add(user);
        if(user.getDepartment() != this){
            user.setDepartment(this);
        }
    }
    public void removeConnectionWithUser(){
        if(users.size() != 0){
            users.stream().forEach(user -> user.setDepartment(null));
        }
    }
}
