package com.mcfuturepartners.crm.api.notice.controller;

import com.mcfuturepartners.crm.api.notice.entity.Notice;
import com.mcfuturepartners.crm.api.notice.service.NoticeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    @ApiOperation(value = "전체 공지사항 전달받기", notes = "날짜별 내림차순 적용완료")
    public ResponseEntity<List<Notice>> getAllNotices(){
        return new ResponseEntity<>( noticeService.getAllNotices(), HttpStatus.OK);
    }
    @GetMapping(value = "/{notice-id}")
    @ApiOperation(value = "상세 공지사항 확인")
    public ResponseEntity<Notice> getNotice(@PathVariable(name ="notice-id") Long noticeId){
        Notice notice = noticeService.getNotice(noticeId);
        if(notice.equals(null)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(notice,HttpStatus.OK);
    }
}
