
package com.glim.user.controller;

import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.service.CustomUserService;
import com.glim.common.security.util.SecurityUtil;
import com.glim.common.statusResponse.StatusResponseDTO;
import com.glim.stories.service.StoryService;
import com.glim.tag.service.ViewTagService;
import com.glim.user.domain.User;
import com.glim.user.dto.request.*;
import com.glim.user.dto.response.*;
import com.glim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;
    private final AwsS3Util awsS3Util;

    // 로그인한 user rate 확인
    @GetMapping("/rate")
    public StatusResponseDTO getCurrentUserRate() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);
        return StatusResponseDTO.ok(user.getRate());
    }
    @PostMapping("/rate")
    public StatusResponseDTO updateCurrentUserRate() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);
        userService.updateRate(userId, user.getRate() == 0 ? 1 : 0);
        return StatusResponseDTO.ok(user.getRate() == 1 ? "정기 구독 완료" : "정기 구독 취소 완료");
    }

    // 로그인한 user role 확인
    @GetMapping("/role")
    public StatusResponseDTO getCurrentUserRole() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);
        return StatusResponseDTO.ok(user.getRole());
    }

    // 로그인한 user 가져오는 api
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal SecurityUserDto user) {
        UserResponse response = userService.getCurrentUserInfo(user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@AuthenticationPrincipal SecurityUserDto user,
                                                           @PathVariable Long id) {
        UserProfileResponse response = userService.getUserProfile(user.getId(), id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.loginAndGenerateTokens(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/oauth-login")
    public ResponseEntity<LoginResponse> socialLogin(@RequestBody OAuthLoginRequest request) {
        LoginResponse response = userService.handleSocialLogin(request);
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/update")
    public ResponseEntity<UserViewResponse> getSimpleUserInfo(@AuthenticationPrincipal SecurityUserDto user) {
        User foundUser = userService.getUserById(user.getId());
        return ResponseEntity.ok(UserViewResponse.from(foundUser, awsS3Util));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal SecurityUserDto user,
                                             @RequestBody UpdateUserRequest request) {
        userService.updateUser(user.getId(), request);
        return ResponseEntity.ok("회원정보 수정 완료");
    }

    @PutMapping("/update-phone")
    public ResponseEntity<String> updatePhone(@AuthenticationPrincipal SecurityUserDto user,
                                              @RequestBody VerifyRequest request) {
        userService.updatePhoneWithVerification(user.getId(), request);
        return ResponseEntity.ok("전화번호 변경 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal SecurityUserDto user) {
        userService.deleteUser(user.getId());
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal SecurityUserDto user,
                                                 @RequestBody ChangePasswordRequest request) {
        userService.changePassword(user.getId(), request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        userService.logout(authorizationHeader);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @PostMapping("/search")
    public ResponseEntity<List<FollowRecommendResponse>> searchUsersByNickname(@RequestBody NicknameSearchRequest request) {
        List<FollowRecommendResponse> result = userService.searchUsersByNickname(request.getNickname());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<FollowRecommendResponse>> getAccountsByPhone(@AuthenticationPrincipal SecurityUserDto user) {
        List<FollowRecommendResponse> result = userService.getAccountsByPhone(user);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/accounts/switch")
    public ResponseEntity<LoginResponse> switchAccount(@RequestBody SwitchAccountRequest request) {
        LoginResponse response = userService.switchAccount(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update-content")
    public ResponseEntity<String> updateContent(@AuthenticationPrincipal SecurityUserDto user,
                                                @RequestBody UpdateContentRequest request) {
        userService.updateContent(user.getId(), request.getContent());
        return ResponseEntity.ok("content 수정 완료");
    }

    @PatchMapping("/update-img")
    public ResponseEntity<String> updateImg(@AuthenticationPrincipal SecurityUserDto user,
                                            @RequestBody UpdateImageRequest request) {
        userService.updateImg(user.getId(), request.getImg());
        return ResponseEntity.ok("img 수정 완료");
    }
}
