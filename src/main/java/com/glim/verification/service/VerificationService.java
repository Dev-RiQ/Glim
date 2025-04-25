package com.glim.verification.service;

import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.repository.AuthCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final AuthCodeRepository authCodeRepository;

    public void verifyCode(String phone, String code) {
        Optional<AuthCodeDocument> optional = authCodeRepository.findTopByPhoneOrderByCreatedAtDesc(phone);

        if (optional.isEmpty() || !optional.get().getCode().equals(code)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }

        AuthCodeDocument auth = optional.get();
        if (auth.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("인증번호가 만료되었습니다.");
        }
    }
}
