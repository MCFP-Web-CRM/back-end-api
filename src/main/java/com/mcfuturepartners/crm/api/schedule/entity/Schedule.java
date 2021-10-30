package com.mcfuturepartners.crm.api.schedule.entity;

import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @Column(name="SCHEDULE_ID")
    private Long scheduleId;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name="TITLE")
    private String title;

    @Column(name="START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "CONTENTS")
    private String contents;

    @Column(name="IS_PUBLIC")
    private Boolean isPublic;

    public Schedule openSchedule(Boolean isPublic){
        this.isPublic = isPublic;
        return this;
    }
}
