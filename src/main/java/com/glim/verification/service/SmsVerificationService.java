package com.glim.verification.service;

import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.repository.AuthCodeRepository;
import com.glim.verification.util.SmsGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsVerificationService {

    private final AuthCodeRepository authCodeRepository;
    private final CoolSmsService coolSmsService;
    private final SmsGeneratorUtil smsGeneratorUtil;

    public String generateAndSendAuthCode(String phone) {
        String code = smsGeneratorUtil.createCode();
        coolSmsService.sendSms(phone, code);
        authCodeRepository.save(new AuthCodeDocument(phone, code, LocalDateTime.now()));
        return code; // ✅ 이걸 꼭 리턴해야 컨트롤러에서 code 쓸 수 있어!
    }

}