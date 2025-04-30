
package com.glim.user.controller;

import com.glim.borad.service.BoardService;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.service.CustomUserService;
import com.glim.common.security.util.SecurityUtil;
import com.glim.user.domain.User;
import com.glim.user.dto.request.*;
import com.glim.user.dto.response.AccessTokenResponse;
import com.glim.user.dto.response.LoginResponse;
import com.glim.user.dto.response.UpdateUserResponse;
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
    private final BoardService boardService;


    // 로그인한 user 가져오는 api
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);
        int boardCount = boardService.countBoardsByUserId(userId);
        return ResponseEntity.ok(UserResponse.from(user, boardCount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        int boardCount = boardService.countBoardsByUserId(id);
        return ResponseEntity.ok(UserResponse.from(user, boardCount));
    }

    // ✅ 로그인: 사용자 인증 후 accessToken + refreshToken + user 응답
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        int boardCount = boardService.countBoardsByUserId(user.getId());
        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user, boardCount), isFirstLogin)
        );
    }

    // 소셜로그인
    @PostMapping("/oauth-login")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody OAuthLoginRequest request) {
        OAuthAttributes oauthAttributes = request.getAttributes();
        String provider = request.getProvider();

        User user = customUserService.saveOrUpdate(oauthAttributes, provider);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        int boardCount = boardService.countBoardsByUserId(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user, boardCount), isFirstLogin)
        );
    }


    // ✅ 회원가입: 신규 사용자 등록
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PostMapping("/check-username")
    public ResponseEntity<String> checkUsername(@RequestBody CheckUsernameRequest request) {
        userService.checkUsernameDuplicate(request.getUsername());
        return ResponseEntity.ok("사용 가능한 아이디입니다!");
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<String> checkNickname(@RequestBody CheckNicknameRequest request) {
        userService.checkNicknameDuplicate(request.getNickname());
        return ResponseEntity.ok("사용 가능한 닉네임입니다!");
    }

    // ✅ 회원정보 조회 (닉네임, 이미지, 이름, 콘텐츠만 조회)
    @GetMapping("/update/{id}")
    public ResponseEntity<UpdateUserResponse> getSimpleUserInfo(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        User targetUser = userService.getUserById(id);

        boolean isFollowing = userService.isFollowing(currentUserId, id); // ✅ 팔로우 여부 확인
        UpdateUserResponse response = UpdateUserResponse.from(targetUser, currentUserId, isFollowing);

        return ResponseEntity.ok(response);
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

    // 전화번호 수정
    @PutMapping("/user/update-phone")
    public ResponseEntity<String> updatePhone(@RequestBody VerifyRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updatePhoneWithVerification(userId, request);
        return ResponseEntity.ok("전화번호 변경 완료");
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

        List<UserResponse> result = users.stream()
                .map(u -> {
                    int boardCount = boardService.countBoardsByUserId(u.getId()); // ✅ 유저별 게시글 수 조회
                    return UserResponse.from(u, boardCount); // ✅ user + boardCount 넘김
                })
                .toList();

        return ResponseEntity.ok(result);
    }

    // 계정 전환 요청
    @PostMapping("/accounts/switch")
    public ResponseEntity<LoginResponse> switchAccount(@RequestBody SwitchAccountRequest request) {
        Long switchToUserId = request.getSwitchToUserId();
        User user = userService.getUserById(switchToUserId);

        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        int boardCount = boardService.countBoardsByUserId(user.getId()); // ✅ 게시글 수 조회

        return ResponseEntity.ok(
                new LoginResponse(
                        accessToken,
                        refreshToken.getToken(),
                        UserResponse.from(user, boardCount), // ✅ 수정
                        false
                )
        );
    }

    // 소개글 수정
    @PatchMapping("/update-content/{id}")
    public ResponseEntity<String> updateContent(@PathVariable Long id, @RequestBody UpdateContentRequest request) {
        userService.updateContent(id, request.getContent());
        return ResponseEntity.ok("content 수정 완료");
    }

    // 이미지 수정
    @PatchMapping("/update-img/{id}")
    public ResponseEntity<String> updateImg(@PathVariable Long id, @RequestBody UpdateImageRequest request) {
        userService.updateImg(id, request.getImg());
        return ResponseEntity.ok("img 수정 완료");
    }

    //  유저 rate 수정
    @PatchMapping("/rate")
    public ResponseEntity<String> updateRate(@RequestBody UpdateRateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updateRate(userId, request.getRate());
        return ResponseEntity.ok("rate 수정 완료");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        AccessTokenResponse accessTokenResponse = userService.refreshAccessToken(request);
        return ResponseEntity.ok(accessTokenResponse);
    }


}
