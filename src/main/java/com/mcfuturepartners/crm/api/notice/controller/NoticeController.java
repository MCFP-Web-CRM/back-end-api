package com.mcfuturepartners.crm.api.notice.controller;

import com.mcfuturepartners.crm.api.notice.entity.Notice;
import com.mcfuturepartners.crm.api.notice.service.NoticeService;
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
    public ResponseEntity<List<Notice>> getAllNotices(){
        return new ResponseEntity<>( noticeService.getAllNotices(), HttpStatus.OK);
    }
    @GetMapping(value = "/{notice-id}")
    public ResponseEntity<Notice> getNotice(@PathVariable(name ="notice-id") Long noticeId){
        Notice notice = noticeService.getNotice(noticeId);
        if(notice.equals(null)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(notice,HttpStatus.OK);
    }
}
