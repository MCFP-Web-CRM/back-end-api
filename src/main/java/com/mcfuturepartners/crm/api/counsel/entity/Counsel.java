package com.mcfuturepartners.crm.api.counsel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Builder
@Table(name = "counsels")
@AllArgsConstructor
@NoArgsConstructor
public class Counsel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    //ManytoOne
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //진행상황
    @Enumerated(EnumType.STRING)
    private CounselStatus status;

    private String contents;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "regdate")
    @NotNull
    private LocalDateTime regDate;

    public Counsel updateModified(Counsel updatedCounsel){
        if(StringUtils.hasText(updatedCounsel.contents)){
            this.setContents(updatedCounsel.contents);
        }
        if(!ObjectUtils.isEmpty(updatedCounsel.status)){
            this.setStatus(updatedCounsel.status);
        }
        if(!ObjectUtils.isEmpty(updatedCounsel.user)){
            this.setUser(updatedCounsel.user);
        }
        return this;
    }
}
