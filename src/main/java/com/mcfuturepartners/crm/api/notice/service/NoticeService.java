package com.mcfuturepartners.crm.api.notice.service;

import com.mcfuturepartners.crm.api.notice.entity.Notice;

import java.util.List;

public interface NoticeService {
    Notice addNotice(Notice notice);
    List<Notice> getAllNotices();
    Notice getNotice(Long noticeId);
    Notice updateNotice(Long noticeId, Notice notice);
    String deleteNotice(Long noticeId);
}
