package com.mcfuturepartners.crm.api.util.sms;

import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.SmsType;
import com.mcfuturepartners.crm.api.sms.dto.SmsDto;
import com.mcfuturepartners.crm.api.sms.dto.SmsProcessDto;
import com.mcfuturepartners.crm.api.sms.dto.SmsResponseDto;
import com.mcfuturepartners.crm.api.sms.entity.Sms;
import com.mcfuturepartners.crm.api.sms.service.SmsService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
@RequiredArgsConstructor
public class SmsRequestHandler {
    private final SmsService smsService;
    private static final String FROM="01067235820";//발신번호
    private static final String accessKey = "xTnNljHbJXm5nJyrq2rs";                                     // 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키
    private static final  String secretKey = "bxJPdKfztwf68aZ89waEyxQwwfia1OCOIGN4vUOU";                // 2차 인증을 위해 서비스마다 할당되는 service secret
    private static final  String serviceId = "ncp:sms:kr:274786160497:mcfuturepartners";                        // 프로젝트에 할당된 SMS 서비스 ID

    @Scheduled(cron = "0 0/1 * * * ?")
    public ResponseEntity processReservedMessage(){
        try{
            smsService.updateReservedSmsTo(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }



    public ResponseEntity sendMessage(@RequestBody SmsProcessDto sms) {
        String pattern2 = "^01(?:0|1|[6-9])[.-]?(-\\d{3}|\\d{4})[.-]?(\\d{4})$";

        String hostNameUrl = "https://sens.apigw.ntruss.com";           // 호스트 URL
        String requestUrl= "/sms/v2/services/";                         // 요청 URL
        String requestUrlType = "/messages";                            // 요청 URL

        String method = "POST";                                         // 요청 method
        String timestamp = Long.toString(System.currentTimeMillis());   // current timestamp (epoch)
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;
        SmsType smsType;
        int count = 0;
        if(sms.getMessage().getContent().length()>80){
            smsType = SmsType.LMS;
        }else{
            smsType = SmsType.SMS;
        }
        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        JSONArray toArr = new JSONArray();

        bodyJson.put("type", smsType.getCode());                // 메시지 Type (sms | lms)
        bodyJson.put("contentType","AD");         // 메시지 내용 Type (AD | COMM) * AD: 광고용, COMM: 일반용 (default: COMM) * 광고용 메시지 발송 시 불법 스팸 방지를 위한 정보통신망법 (제 50조)가 적용됩니다.
        bodyJson.put("countryCode","82");       // 국가 전화번호
        bodyJson.put("from",FROM);              // 발신번호 * 사전에 인증/등록된 번호만 사용할 수 있습니다.
        bodyJson.put("subject","");             // 메시지 제목 * LMS Type에서만 사용할 수 있습니다.
        bodyJson.put("content",sms.getMessage().getContent());              // 메시지 내용 * Type별로 최대 byte 제한이 다릅니다.* SMS: 80byte / LMS: 2000byte
        bodyJson.put("messages", toArr);
        if(sms.getReceiverPhone().size()==0){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else{
            for(int i=0; i<sms.getReceiverPhone().size(); i++) {
                String phone = sms.getReceiverPhone().get(i);
                if(!Pattern.matches(pattern2,sms.getReceiverPhone().get(i))) {
                    continue;
                }
                phone = phone.replace("-","");

                JSONObject toJson = new JSONObject();
                toJson.put("subject","");                           // 메시지 제목 * LMS Type에서만 사용할 수 있습니다.
                toJson.put("content",sms.getMessage().getContent());                // 메시지 내용 * Type별로 최대 byte 제한이 다릅니다.* SMS: 80byte / LMS: 2000byte
                toJson.put("to",phone);       // 수신번호 목록  * 최대 1000개까지 한번에 전송할 수 있습니다.
                toArr.add(toJson);
                count++;
            }
        }

        if(!ObjectUtils.isEmpty(sms.getReservationTime())){
            bodyJson.put("reserveTime", sms.getReservationTime().toString().replace("T"," "));
        }

        String body = bodyJson.toJSONString();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("content-type", "application/json; charset=utf-8");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
            con.setRequestMethod(method);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            System.out.println("con = " + con.getOutputStream().toString());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            log.info(con.getResponseMessage());
            if(responseCode==202) { // 정상 호출
                return new ResponseEntity(HttpStatus.OK);
            } else {  // 에러 발생
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public static String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";                    // one space
        String newLine = "\n";                 // new line
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {
            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            encodeBase64String = e.toString();
        }
        return encodeBase64String;
    }
}
