package com.mcfuturepartners.crm.api.sms.entity;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.department.entity.Department;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "sms")
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Sms {
    @Id
    @Column(name = "sms_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smsId;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(name = "sms_status")
    private SmsStatus smsStatus;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User sender;

}
