package com.mcfuturepartners.crm.api.notice.service;

import com.mcfuturepartners.crm.api.exception.DatabaseErrorCode;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.notice.entity.Notice;
import com.mcfuturepartners.crm.api.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;

    @Override
    public Notice addNotice(Notice notice) {
        return noticeRepository.save(notice.writeNow());
    }

    public List<Notice> getAllNotices(){
        return noticeRepository.findAll().stream().filter(notice -> notice.getRegDate() != null).sorted((o1,o2)->o2.getRegDate().compareTo(o1.getRegDate())).collect(Collectors.toList());
    }

    public Notice getNotice(Long noticeId){
        return noticeRepository.findById(noticeId).orElse(null);
    }

    @Override
    public Notice updateNotice(Long noticeId, Notice notice) {
        Notice originalNotice = noticeRepository.findById(noticeId).orElseThrow(()-> new FindException("NOTICE "+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        return noticeRepository.save(originalNotice.updateModified(notice));
    }

    @Override
    public String deleteNotice(Long noticeId) {
        try{
            noticeRepository.deleteById(noticeId);
        }catch(Exception e){
            throw e;
        }
        return "Deleted Successfully";
    }

}
