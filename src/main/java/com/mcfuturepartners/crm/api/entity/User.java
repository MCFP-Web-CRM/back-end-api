package com.mcfuturepartners.crm.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    @NotNull
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100)
    @NotNull
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();
}
