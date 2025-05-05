package com.glim.user.controller;

import com.glim.common.security.util.SecurityUtil;
import com.glim.user.dto.request.SocialInfoRequest;
import com.glim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ UserController.java - 추가 정보 입력 API
    @PostMapping("/social-info")
    public ResponseEntity<String> completeSocialInfo(@RequestBody SocialInfoRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.completeSocialInfo(userId, request); // ✅ Service로 위임
        return ResponseEntity.ok("추가 정보 입력 완료!");
    }

}
