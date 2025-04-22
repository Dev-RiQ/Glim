
package com.glim.verification.controller;
import com.glim.verification.service.SmsVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/verify")
@RequiredArgsConstructor
public class SmsVerificationController {

    private final SmsVerificationService smsVerificationService;

    @PostMapping("/request")
    public ResponseEntity<String> requestCode(@RequestParam String phone) {
        smsVerificationService.generateAndSendAuthCode(phone);
        return ResponseEntity.ok("인증번호 전송 완료");
    }
}