package com.glim.user.service;

import com.glim.borad.service.BoardService;
import com.glim.chating.service.ChatUserService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.domain.FileType;
import com.glim.common.awsS3.service.AwsS3Service;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.service.CustomUserService;
import com.glim.common.security.util.SecurityUtil;
import com.glim.notification.service.NotificationService;
import com.glim.stories.service.StoryService;
import com.glim.tag.domain.ViewTag;
import com.glim.tag.repository.ViewTagRepository;
import com.glim.user.domain.PlatForm;
import com.glim.user.domain.User;
import com.glim.user.dto.request.*;
import com.glim.user.dto.response.*;
import com.glim.user.repository.FollowRepository;
import com.glim.user.repository.UserRepository;
import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.repository.AuthCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Util awsS3Util;
    private final BoardService boardService;
    private final FollowService followService;
    private final NotificationService notificationService;
    private final StoryService storyService;
    private final ChatUserService chatUserService;
    private final AuthCodeRepository authCodeRepository;
    private final ViewTagRepository viewTagRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserService customUserService;
    private final AwsS3Service awsS3Service;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AwsS3Util awsS3Util,
            BoardService boardService,
            @Lazy FollowService followService,
            @Lazy NotificationService notificationService,
            StoryService storyService,
            ChatUserService chatUserService,
            AuthCodeRepository authCodeRepository,
            ViewTagRepository viewTagRepository, RefreshTokenService refreshTokenService, JwtTokenProvider jwtTokenProvider, FollowRepository followRepository, CustomUserService customUserService, AwsS3Service awsS3Service) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.awsS3Util = awsS3Util;
        this.boardService = boardService;
        this.followService = followService;
        this.notificationService = notificationService;
        this.storyService = storyService;
        this.chatUserService = chatUserService;
        this.authCodeRepository = authCodeRepository;
        this.viewTagRepository = viewTagRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserService = customUserService;
        this.awsS3Service = awsS3Service;
    }

    // ✅ 현재 로그인한 사용자의 마이페이지 정보 가져오기
    public UserResponse getCurrentUserInfo(Long userId) {
        User foundUser = getUserById(userId);
        int boardCount = boardService.countBoardsByUserId(userId);
        boolean isStory = storyService.isStory(userId);
        String url = awsS3Util.getURL(foundUser.getImg(), FileSize.IMAGE_128);
        return UserResponse.from(foundUser, boardCount, isStory, url);
    }

    // ✅ 특정 사용자 프로필 정보 가져오기
    public UserProfileResponse getUserProfile(Long requesterId, Long targetUserId) {
        User targetUser = getUserById(targetUserId);
        int boardCount = boardService.countBoardsByUserId(targetUserId);
        boolean isFollowing = followService.isFollowing(requesterId, targetUserId);  // 여기!
        boolean isStory = storyService.isStory(targetUserId);
        String img = awsS3Util.getURL(targetUser.getImg(), FileSize.IMAGE_128);
        return UserProfileResponse.from(requesterId, targetUser, boardCount, isFollowing, isStory, img);
    }

    // ✅ 로컬 로그인 처리 및 토큰 생성
    public LoginResponse loginAndGenerateTokens(LoginRequest request) {
        User user = login(request);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        int boardCount = boardService.countBoardsByUserId(user.getId());
        boolean isStory = storyService.isStory(user.getId());
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                UserResponse.from(user, boardCount, isStory, img),
                isFirstLogin
        );
    }

    // ✅ 로컬 로그인 내부 인증 (비밀번호 검사 포함)
    public User login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getUsername().startsWith("test")) {
            if (!request.getPassword().equals(user.getPassword())) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }
        }
        return user;
    }
    // ✅ 소셜 로그인 처리 및 토큰 생성
    @Transactional
    public LoginResponse handleSocialLogin(OAuthLoginRequest request) {
        OAuthAttributes oauthAttributes = request.getAttributes();
        String provider = request.getProvider();

        User user = customUserService.saveOrUpdate(oauthAttributes, provider);
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        int boardCount = boardService.countBoardsByUserId(user.getId());
        boolean isStory = storyService.isStory(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);

        return new LoginResponse(accessToken, refreshToken.getToken(), UserResponse.from(user, boardCount, isStory, img), isFirstLogin);
    }

    // ✅ 회원가입 처리
    @Transactional
    public void registerUser(AddUserRequest request) {
        long existing = userRepository.countByPhoneAndPlatForm(request.getPhone(), PlatForm.LOCAL);

        if (existing >= 2) {
            throw new CustomException(ErrorCode.PHONE_ACCOUNT_LIMIT);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        User user = request.toEntity();

        user.encodePassword(passwordEncoder); // 비밀번호 암호화
        userRepository.save(user);
    }

    // ✅ 아이디(Username) 중복 검사
    @Transactional(readOnly = true)
    public void checkUsernameDuplicate(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    // ✅ 닉네임(Nickname) 중복 검사
    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }

    // ✅ 회원 정보 수정
    @Transactional
    public void updateUser(Long userId, UpdateUserRequest request) {
        // 🔍 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // ✅ 닉네임 중복 검사
        if (request.getNickname() != null &&
                !user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // ✅ 닉네임 수정
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        // ✅ 프로필 이미지 수정
        if (request.getImg() != null) {
            String resizedImgUrl = awsS3Util.getURL(request.getImg(), FileSize.IMAGE_128);
            user.setImg(resizedImgUrl);
        }

        // ✅ 이름 수정
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        // ✅ 소개글 수정
        if (request.getContent() != null) {
            user.setContent(request.getContent());
        }

    }

    // ✅ 회원 탈퇴 처리 (연관 데이터 삭제)
    @Transactional
    public void deleteUser(Long id) {
        // 1. Board 관련 삭제(좋아요, 댓글, 댓글 좋아요)
        boardService.deleteBoardRelatedDataByUser(id);

        // 2. Follow 삭제
        followService.deleteFollowByUser(id);

        // 3. Notification 삭제
        notificationService.deleteNotificationsByUser(id);

        // 4. Story 삭제
        storyService.deleteStoriesByUser(id);

        // 5. ChatUser 삭제
        chatUserService.deleteChatUsersByUser(id);

        // 6. User 이미지 파일
        User user = userRepository.findUserById(id);
        if(!user.getImg().equals("userimages/user-default-image")){
            awsS3Service.deleteFile(user.getImg(), FileType.USER_IMAGE);
        }

        // 7. User 삭제
        userRepository.delete(user);
    }

    // ✅ 비밀번호 변경
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호 인코딩 후 저장
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // ✅ 특정 사용자 조회 (ID 기준)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // ✅ 닉네임으로 유저 검색 (20개 제한)
    public List<FollowRecommendResponse> searchUsersByNickname(String keyword) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        List<User> users = userRepository.findTop20ByNicknameContainingIgnoreCase(keyword);

        return users.stream()
                .filter(user -> !user.getId().equals(currentUserId))  // ✅ 본인 제외
                .map(user -> {
                    boolean isStory = storyService.isStory(user.getId());
                    user.setImg(awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128));
                    return FollowRecommendResponse.from(user,isStory);
                })
                .toList();
    }

    // ✅ 전화번호로 유저 목록 조회
    public List<User> findByPhone(String phone) {
        return userRepository.findAllByPhone(phone);
    }

    // ✅ 비밀번호 업데이트 (비밀번호 찾기용)
    @Transactional
    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // ✅ 전화번호로 아이디(Username) 찾기
    public String findUsernameByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getUsername();
    }

    // ✅ 마지막 읽은 알람 ID 업데이트
    @Transactional
    public void updateReadAlarmId(Long id, Long readAlarmId) {
        User user = getUserById(id);   // 기존 메서드 사용해서 유저 찾고
        user.updateReadAlarmId(readAlarmId); // 새 필드 업데이트
        userRepository.save(user);    // 저장
    }

    // ✅ 마지막 읽은 게시글 ID 업데이트
    @Transactional
    public void updateReadBoardId(Long id, Long readBoardId) {
        User user = getUserById(id);
        user.updateReadBoardId(readBoardId);
        userRepository.save(user);
    }

    // ✅ 마지막 읽은 게시글 id 조회
    public Long getReadBoardId(Long id) {
        User user = getUserById(id);
        return user.getReadBoardId();
    }

    // ✅ 마지막 읽은 알람 id 조회
    public Long getReadAlarmId(Long id) {
        User user = getUserById(id);
        return user.getReadAlarmId();
    }

    // ✅ 상위 20개 태그 가져오기
    public List<ViewTag> getTop20ViewTags(Long userId) {
        return viewTagRepository.findTop20ByUserIdOrderByViewsDesc(userId);
    }

    // ✅ 업데이트 소개글
    @Transactional
    public void updateContent(Long id, String content) {
        User user = getUserById(id);
        user.updateContent(content);
        userRepository.save(user);
    }

    // ✅ 업데이트 이미지
    @Transactional
    public void updateImg(Long id, String img) {
        User user = getUserById(id);
        user.updateImg(img);
        userRepository.save(user);
    }

    // ✅ 업데이트 구매등급
    @Transactional
    public void updateRate(Long userId, Integer rate) {
        User user = getUserById(userId);
        user.updateRate(rate);
    }

    // ✅ 소셜로그인 최초 이용자 추가 정보 입력
    @Transactional
    public void completeSocialInfo(SocialInfoRequest request) {
        Long userId = jwtTokenProvider.getUserId(request.getAccessToken());
        User user = getUserById(userId); // ✅ 기존 메서드 재사용

        // 닉네임 중복 검사
        if (request.getNickname() != null && userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 추가 정보 저장
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        user.setImg("userimages/user-default-image");
        userRepository.save(user);
    }

    // ✅ 전화번호 업데이트 (인증번호 확인 후)
    @Transactional
    public void updatePhoneWithVerification(Long userId, VerifyRequest request) {
        // 인증번호 검증
        AuthCodeDocument authCode = authCodeRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE));

        if (!authCode.getCode().equals(request.getCode())) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        // 유효 시간 초과 체크 (예: 3분 제한)
        if (authCode.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        // 유저 찾아서 전화번호 업데이트
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePhone(request.getPhone());
    }

    // ✅ 로그아웃 (리프레시 토큰 삭제)
    @Transactional
    public void logout(String authorizationHeader) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        refreshTokenService.deleteByUserId(userId);
    }

    // ✅ 같은 전화번호 기반 다른 계정 목록 가져오기
    public List<FollowRecommendResponse> getAccountsByPhone(SecurityUserDto user) {
        List<User> users = findByPhone(user.getPhone());

        return users.stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .map(u -> {
                    boolean isStory = storyService.isStory(u.getId());
                    u.setImg(awsS3Util.getURL(u.getImg(), FileSize.IMAGE_128));
                    return FollowRecommendResponse.from(u, isStory);
                })
                .toList();
    }

    // ✅ 계정 전환 처리 (새 토큰 발급)
    @Transactional
    public LoginResponse switchAccount(SwitchAccountRequest request) {
        Long switchToUserId = request.getSwitchToUserId();
        User user = getUserById(switchToUserId);

        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        int boardCount = boardService.countBoardsByUserId(user.getId());
        boolean isStory = storyService.isStory(user.getId());
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                UserResponse.from(user, boardCount, isStory, img),
                false
        );
    }    /**
     * 해당 휴대폰으로 가입된 LOCAL 계정의 username을 최대 2개까지 반환
     */
    public List<String> findLocalUsernamesByPhone(String phone) {
        return userRepository
                // platForm 필드로 LOCAL 계정만 조회
                .findAllByPhoneAndPlatForm(phone, PlatForm.LOCAL)
                .stream()
                .map(User::getUsername)
                .limit(2)
                .collect(Collectors.toList());
    }

}




