
package com.glim.verification.controller;

import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.verification.dto.PhoneRequest;
import com.glim.verification.service.SmsVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class SmsVerificationController {

    private final SmsVerificationService smsVerificationService;

    @PostMapping("/request")
    public StatusResponseDTO requestCode(@RequestBody PhoneRequest request) {
        String code = smsVerificationService.generateAndSendAuthCode(request.getPhone()); // ✅ 여기에 code 받아오기
        return StatusResponseDTO.ok(code);
    }

}