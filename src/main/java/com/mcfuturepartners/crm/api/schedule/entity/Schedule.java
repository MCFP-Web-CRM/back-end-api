package com.mcfuturepartners.crm.api.schedule.entity;

import com.mcfuturepartners.crm.api.schedule.dto.ScheduleUpdate;
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

@Entity
@Data
@Builder
@Table(name = "schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @Column(name="SCHEDULE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name="TITLE")
    @NotNull
    private String title;

    @Column(name="START_DATE")
    @NotNull
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    @NotNull
    private LocalDateTime endDate;

    @Column(name = "CONTENTS")
    private String contents;

    @Column(name="IS_PUBLIC")
    @NotNull
    private Boolean isPublic;

    public Schedule updateModified(ScheduleUpdate scheduleUpdate){
        if(StringUtils.hasText(scheduleUpdate.getTitle())){
           this.setTitle(scheduleUpdate.getTitle());
        }
        if (StringUtils.hasText(scheduleUpdate.getContents())){
            this.setContents(scheduleUpdate.getContents());
        }
        if(!ObjectUtils.isEmpty(scheduleUpdate.getStartDate())){
            this.setStartDate(scheduleUpdate.getStartDate());
        }
        if(!ObjectUtils.isEmpty(scheduleUpdate.getEndDate())){
            this.setEndDate(scheduleUpdate.getEndDate());
        }
        if(!ObjectUtils.isEmpty(scheduleUpdate.getIsPublic())){
            this.setIsPublic(scheduleUpdate.getIsPublic());
        }

        return this;
    }
}
