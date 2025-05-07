package com.glim.user.controller;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.user.domain.ResetToken;
import com.glim.user.dto.request.FindPasswordRequest;
import com.glim.user.dto.request.FindUsernameRequest;
import com.glim.user.dto.request.ResetPasswordRequest;
import com.glim.user.dto.response.ResetTokenResponse;
import com.glim.user.service.ResetTokenService;
import com.glim.user.service.UserService;
import com.glim.verification.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final ResetTokenService resetTokenService;
    private final UserService userService;
    private final VerificationService verificationService;

    // ✅ 아이디 찾기 - 본인인증 후 아이디 공개 ( 마스킹 )
    @PostMapping("/find-username")
    public ResponseEntity<List<String>> findUsername(
            @RequestBody FindUsernameRequest request
    ) {
        // 1) SMS 인증 코드 확인
        verificationService.verifyCode(request.getPhone(), request.getCode());

        // 2) LOCAL 계정 중 휴대폰 번호로 아이디 최대 2개 조회
        List<String> usernames = userService.findLocalUsernamesByPhone(request.getPhone());

        // 3) 각 아이디에 대해 maskUsername 적용
        List<String> masked = usernames.stream()
                .map(this::maskUsername)
                .collect(Collectors.toList());

        return ResponseEntity.ok(masked);
    }

    private String maskUsername(String username) {
        List<Integer> positions = Arrays.asList(2, 3, 6);

        StringBuilder sb = new StringBuilder(username);
        for (Integer pos : positions) {
            int idx = pos - 1;               // 0-base index 로 변환
            if (idx >= 0 && idx < sb.length()) {
                sb.setCharAt(idx, '*');      // 해당 위치를 '*' 로 대체
            }
        }
        return sb.toString();
    }

    // ✅ 비밀번호 찾기 - 본인인증 후 resetToken 발급
    private boolean isTokenExpired(LocalDateTime createdAt) {
        return createdAt.plusMinutes(10).isBefore(LocalDateTime.now());
    }

    // ✅ 비밀번호 찾기 - 인증번호 검증 후 resetToken 발급
    @PostMapping("/find-password")
    public ResponseEntity<ResetTokenResponse> findPassword(@RequestBody FindPasswordRequest request) {
        verificationService.verifyCode(request.getPhone(), request.getCode());
        String resetToken = resetTokenService.createResetToken(request.getUsername());
        return ResponseEntity.ok(new ResetTokenResponse(resetToken));
    }

    // ✅ 비밀번호 재설정 - resetToken 인증 후 비밀번호 변경
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String username = resetTokenService.getUsernameByToken(request.getResetToken())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESET_TOKEN));

        ResetToken tokenEntity = resetTokenService.getTokenEntity(request.getResetToken())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESET_TOKEN));

        // 직접 시간 만료 체크
        if (isTokenExpired(tokenEntity.getCreatedAt())) {
            throw new CustomException(ErrorCode.EXPIRED_RESET_TOKEN);
        }

        if (!username.equals(request.getUsername())) {
            throw new CustomException(ErrorCode.INVALID_RESET_TOKEN);
        }

        userService.updatePassword(request.getUsername(), request.getNewPassword());
        resetTokenService.deleteToken(request.getResetToken());

        return ResponseEntity.ok("비밀번호 재설정 완료");
    }



}
