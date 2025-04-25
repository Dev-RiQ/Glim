
package com.glim.verification.controller;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.dto.request.PhoneRequest;
import com.glim.verification.dto.request.PhoneVerifyRequest;
import com.glim.verification.dto.response.VerificationResultResponse;
import com.glim.verification.repository.AuthCodeRepository;
import com.glim.verification.service.SmsVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
public class SmsVerificationController {

    private final SmsVerificationService smsVerificationService;
    private final AuthCodeRepository authCodeRepository;

    @PostMapping("/request") // 인증번호 요청 ( mogodb에 저장 )
    public StatusResponseDTO requestCode(@RequestBody PhoneRequest request) {
        String code = smsVerificationService.generateAndSendAuthCode(request.getPhone()); // ✅ 여기에 code 받아오기
        return StatusResponseDTO.ok(code);
    }

    @PostMapping("/verifyCode") // 인증번호 검증
    public ResponseEntity<VerificationResultResponse> verifyCode(@RequestBody PhoneVerifyRequest request) {
        AuthCodeDocument authCode = authCodeRepository
                .findTopByPhoneOrderByCreatedAtDesc(request.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE));

        if (authCode.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        if (!authCode.getCode().equals(request.getCode())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        return ResponseEntity.ok(new VerificationResultResponse(true, "인증 성공!"));
    }

}