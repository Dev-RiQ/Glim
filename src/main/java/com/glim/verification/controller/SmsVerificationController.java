package com.glim.verification.controller;

import com.glim.verification.service.SmsVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/verify")
public class SmsVerificationController {

    private final SmsVerificationService smsVerificationService;

    // 인증번호 생성 요청
    @PostMapping("/request")
    public ResponseEntity<String> requestCode(@RequestParam String phone) {
        String code = smsVerificationService.generateAndSaveAuthCode(phone);
        return ResponseEntity.ok("인증번호: " + code); // 실제 운영에서는 문자 전송 후 "전송 완료"만 반환
    }

    // 인증번호 확인
    @PostMapping("/check")
    public ResponseEntity<String> checkCode(@RequestParam String phone,
                                            @RequestParam String code) {
        boolean isValid = smsVerificationService.verifyAuthCode(phone, code);
        return isValid
                ? ResponseEntity.ok("인증 성공")
                : ResponseEntity.badRequest().body("인증 실패");
    }
}
