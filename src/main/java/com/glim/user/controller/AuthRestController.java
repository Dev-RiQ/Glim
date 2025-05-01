
package com.glim.user.controller;

import com.glim.borad.service.BoardService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.service.CustomUserService;
import com.glim.common.security.util.SecurityUtil;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.User;
import com.glim.user.dto.request.*;
import com.glim.user.dto.response.*;
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
    private final AwsS3Util awsS3Util;
    private final StoryService storyService;

    // 로그인한 user 가져오는 api
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(userId);
        int boardCount = boardService.countBoardsByUserId(userId);
        boolean isStory = storyService.isStory(userId);
        String url = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);
        return ResponseEntity.ok(UserResponse.from(user, boardCount, isStory,url));
    }

    // 회원정보
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        Long currentUserId = SecurityUtil.getCurrentUserId(); // 로그인한 사용자 ID
        User targetUser = userService.getUserById(id);
        int boardCount = boardService.countBoardsByUserId(id);
        boolean isFollowing = userService.isFollowing(currentUserId, id);
        boolean isStory = storyService.isStory(currentUserId);
        UserProfileResponse response = UserProfileResponse.from(currentUserId, targetUser, boardCount, isFollowing, isStory);
        return ResponseEntity.ok(response);
    }


    // ✅ 로그인: 사용자 인증 후 accessToken + refreshToken + user 응답
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        int boardCount = boardService.countBoardsByUserId(user.getId());
        boolean isStory = storyService.isStory(user.getId());
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);
        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user, boardCount, isStory, img), isFirstLogin)
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
        boolean isStory = storyService.isStory(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);
        return ResponseEntity.ok(
                new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user, boardCount, isStory, img), isFirstLogin)
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

    // ✅ 마이페이지 조회 (닉네임, 이미지, 이름, 콘텐츠만 조회)
    @GetMapping("/update")
    public ResponseEntity<UserViewResponse> getSimpleUserInfo() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        User user = userService.getUserById(currentUserId);
        return ResponseEntity.ok(UserViewResponse.from(user, awsS3Util));
    }

    // ✅ 마이페이지 수정: 로그인한 사용자만 본인 정보 수정 가능
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.updateUser(currentUserId, request);
        return ResponseEntity.ok("회원정보 수정 완료");
    }

    // 전화번호 수정
    @PutMapping("/update-phone")
    public ResponseEntity<String> updatePhone(@RequestBody VerifyRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        userService.updatePhoneWithVerification(userId, request);
        return ResponseEntity.ok("전화번호 변경 완료");
    }

    // ✅ 회원 탈퇴: 로그인한 사용자만 본인 탈퇴 가능
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.deleteUser(currentUserId);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    // ✅ 비밀번호 변경: 로그인한 사용자만 본인 비밀번호 수정 가능
    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.changePassword(currentUserId, request);
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
    @PostMapping("/search")
    public ResponseEntity<List<FollowRecommendResponse>> searchUsersByNickname(@RequestBody NicknameSearchRequest request) {
        List<FollowRecommendResponse> result = userService.searchUsersByNickname(request.getNickname());
        return ResponseEntity.ok(result);
    }

    // 전화번호 기반 유저 목록 조회
    @GetMapping("/accounts")
    public ResponseEntity<List<FollowRecommendResponse>> getAccountsByPhone(@AuthenticationPrincipal SecurityUserDto user) {
        List<User> users = userService.findByPhone(user.getPhone());

        List<FollowRecommendResponse> result = users.stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .map(u -> {
                    boolean isStory = storyService.isStory(u.getId());
                    return FollowRecommendResponse.from(u, isStory);
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
        boolean isStory = storyService.isStory(user.getId());
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);

        return ResponseEntity.ok(
                new LoginResponse(
                        accessToken,
                        refreshToken.getToken(),
                        UserResponse.from(user, boardCount, isStory, img), // ✅ 수정
                        false
                )
        );
    }

    // 소개글 수정
    @PatchMapping("/update-content")
    public ResponseEntity<String> updateContent(@RequestBody UpdateContentRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.updateContent(currentUserId, request.getContent());
        return ResponseEntity.ok("content 수정 완료");
    }

    // 이미지 수정
    @PatchMapping("/update-img")
    public ResponseEntity<String> updateImg(@RequestBody UpdateImageRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        userService.updateImg(currentUserId, request.getImg());
        return ResponseEntity.ok("img 수정 완료");
    }




}
