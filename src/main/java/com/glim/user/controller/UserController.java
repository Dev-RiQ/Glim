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
        userService.completeSocialInfo(userId, request); // ✅ Service로 위임
        return ResponseEntity.ok("추가 정보 입력 완료!");
    }

}
