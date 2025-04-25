
package com.glim.user.controller;

import com.amazonaws.services.s3.model.CSVOutput;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.dto.RefreshTokenRequest;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.service.CustomUserService;
import com.glim.common.security.util.SecurityUtil;
import com.glim.user.domain.User;
import com.glim.user.dto.request.FindPasswordRequest;
import com.glim.user.dto.request.ResetPasswordRequest;
import com.glim.user.dto.request.*;
import com.glim.user.dto.response.LoginResponse;
import com.glim.user.dto.response.UserResponse;
import com.glim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final CustomUserService customUserService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;


    // 로그인한 user 가져오는 api
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponse.from(user));
    }


    // ✅ 로그인: 사용자 인증 후 accessToken + refreshToken + user 응답
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);

        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user), isFirstLogin)
        );
    }

    @PostMapping("/oauth-login")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody OAuthLoginRequest request) {
        OAuthAttributes oauthAttributes = request.getAttributes();
        String provider = request.getProvider();

        User user = customUserService.saveOrUpdate(oauthAttributes, provider);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user), isFirstLogin)
        );
    }


    // ✅ 회원가입: 신규 사용자 등록
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // ✅ 회원정보 수정: 로그인한 사용자만 본인 정보 수정 가능
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (!id.equals(currentUserId)) {
            return ResponseEntity.status(403).body("본인만 수정할 수 있습니다.");
        }
        userService.updateUser(id, request);
        return ResponseEntity.ok("회원정보 수정 완료");
    }

    // ✅ 회원 탈퇴: 로그인한 사용자만 본인 탈퇴 가능
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (!id.equals(currentUserId)) {
            return ResponseEntity.status(403).body("본인만 탈퇴할 수 있습니다.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    // ✅ 비밀번호 변경: 로그인한 사용자만 본인 비밀번호 수정 가능
    @PatchMapping("/password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (!id.equals(currentUserId)) {
            return ResponseEntity.status(403).body("본인만 비밀번호를 수정할 수 있습니다.");
        }
        userService.changePassword(id, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // ✅ 로그아웃: accessToken 헤더 기반, refreshToken 삭제
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }


    // 🔍 닉네임 검색 API
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsersByNickname(@RequestParam String nickname) {
        List<UserResponse> result = userService.searchUsersByNickname(nickname);
        return ResponseEntity.ok(result);
    }

    // 전화번호 기반 유저 목록 조회
    @GetMapping("/accounts")
    public ResponseEntity<List<UserResponse>> getAccountsByPhone(@AuthenticationPrincipal SecurityUserDto user) {
        List<User> users = userService.findByPhone(user.getPhone());
        List<UserResponse> result = users.stream().map(UserResponse::from).toList();
        return ResponseEntity.ok(result);
    }

    // 계정 전환 요청
    @PostMapping("/accounts/switch")
    public ResponseEntity<LoginResponse> switchAccount(@RequestBody SwitchAccountRequest request) {
        Long switchToUserId = request.getSwitchToUserId();
        User user = userService.getUserById(switchToUserId);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user), false));
    }






}
