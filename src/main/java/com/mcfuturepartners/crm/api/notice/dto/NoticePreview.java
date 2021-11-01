package com.mcfuturepartners.crm.api.notice.dto;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class NoticePreview {

    private Long noticeId;

    private LocalDateTime regDate;

    private String title;
}
