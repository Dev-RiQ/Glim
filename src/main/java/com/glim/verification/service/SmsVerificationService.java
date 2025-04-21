package com.glim.verification.service;

import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.repository.AuthCodeRepository;
import com.glim.verification.util.SmsGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SmsVerificationService {

    private final AuthCodeRepository authCodeRepository;

    // 인증번호 생성 및 저장
    public String generateAndSaveAuthCode(String phone) {
        String code = SmsGeneratorUtil.generateAuthCode(6); // 6자리 랜덤 숫자 생성

        AuthCodeDocument doc = new AuthCodeDocument(
                phone,
                code,
                Instant.now().plusSeconds(180) // 3분 뒤 만료 시간 설정
        );

        authCodeRepository.save(doc);
        return code;
    }

    // 인증번호 검증
    public boolean verifyAuthCode(String phone, String inputCode) {
        return authCodeRepository.findByPhone(phone)
                .filter(doc -> doc.getCode().equals(inputCode))                // 코드 일치 확인
                .filter(doc -> Instant.now().isBefore(doc.getExpiresAt()))    // 아직 유효한지 확인
                .isPresent();
    }
}
