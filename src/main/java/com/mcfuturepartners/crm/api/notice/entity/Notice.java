package com.mcfuturepartners.crm.api.notice.entity;

import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "notice")
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private Long noticeId;

    @Column(name="REGISTER_DATE")
    private LocalDateTime regDate;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENTS")
    private String contents;

    public Notice writeNow(){
        regDate = LocalDateTime.now();
        return this;
    }
    public Notice updateModified(Notice notice){
        if(ObjectUtils.isEmpty(notice.getTitle())&&ObjectUtils.isEmpty(notice.getContents())){
            return this;
        }
        if(!ObjectUtils.isEmpty(notice.getTitle())){
            this.title = notice.getTitle();
        }
        if(!ObjectUtils.isEmpty(notice.getContents())){
            this.contents = notice.getContents();
        }
        return writeNow();
    }
}
