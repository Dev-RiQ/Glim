package com.glim.user.controller;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.util.SecurityUtil;
import com.glim.user.domain.User;
import com.glim.user.dto.request.SocialInfoRequest;
import com.glim.user.repository.UserRepository;
import com.glim.user.service.UserService;
import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.repository.AuthCodeRepository;
import com.glim.verification.service.SmsVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthCodeRepository authCodeRepository;

    // ✅ UserController.java - 추가 정보 입력 API
    @PostMapping("/social-info")
    public ResponseEntity<String> completeSocialInfo(@RequestBody SocialInfoRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);

        if (request.getNickname() != null && userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // ✅ 인증번호 유효성 검사 (Mongo에 저장된 인증 코드 기준 + 3분 제한 체크)
        AuthCodeDocument authCode = authCodeRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE));

        // ⏱️ 인증번호 유효 시간: 3분
        if (authCode.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        if (!authCode.getCode().equals(request.getVerificationCode())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        boolean isVerified = authCode.getCode().equals(request.getVerificationCode());
        if (!isVerified) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
        if (!isVerified) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        userRepository.save(user);

        return ResponseEntity.ok("추가 정보 입력 완료!");
    }
}
