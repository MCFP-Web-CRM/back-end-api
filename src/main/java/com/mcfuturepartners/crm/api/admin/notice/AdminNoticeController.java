package com.mcfuturepartners.crm.api.admin.notice;

import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.notice.entity.Notice;
import com.mcfuturepartners.crm.api.notice.service.NoticeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {
    private final NoticeService noticeService;

    @PostMapping
    @ApiOperation(value = "공지사항 추가", notes = "공지사항 추가 / 관리자 계정만 가능(현재는 url 막지 않아서 가능)")
    public ResponseEntity<Notice> addNotice(@RequestBody Notice notice){
        Notice originalNotice;
        try {
            originalNotice = noticeService.addNotice(notice);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(originalNotice, HttpStatus.CREATED);
    }
    @PutMapping(value = "{notice-id}")
    @ApiOperation(value = "공지사항 수정", notes = "공지사항 수정 / 관리자 계정만 가능(현재는 url 막지 않아서 가능)")
    public ResponseEntity<Notice> updateNotice(@PathVariable(name = "notice-id") Long noticeId,
                                              @RequestBody Notice notice){
        Notice updatedNotice = new Notice();
        //stacktrace, 예외 처리 관련 테스트 update와 delete  비교
        try{
            updatedNotice = noticeService.updateNotice(noticeId,notice);
        }catch(FindException f){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedNotice,HttpStatus.OK);
    }
    @DeleteMapping(value = "{notice-id}")
    @ApiOperation(value = "공지사항 삭제", notes = "공지사항 삭제 / 관리자 계정만 가능(현재는 url 막지 않아서 가능)")
    public ResponseEntity<Void> deleteNotice(@PathVariable(name = "notice-id") Long noticeId){

        if(StringUtils.hasText(noticeService.deleteNotice(noticeId))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
