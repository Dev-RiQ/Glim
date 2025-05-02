package com.glim.user.service;

import com.glim.borad.service.BoardService;
import com.glim.chating.service.ChatUserService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.common.security.util.SecurityUtil;
import com.glim.notification.service.NotificationService;
import com.glim.stories.service.StoryService;
import com.glim.tag.domain.ViewTag;
import com.glim.tag.repository.ViewTagRepository;
import com.glim.user.domain.User;
import com.glim.user.dto.request.*;
import com.glim.user.dto.response.AccessTokenResponse;
import com.glim.user.dto.response.FollowRecommendResponse;
import com.glim.user.dto.response.LoginResponse;
import com.glim.user.dto.response.UserResponse;
import com.glim.user.repository.FollowRepository;
import com.glim.user.repository.UserRepository;
import com.glim.verification.domain.AuthCodeDocument;
import com.glim.verification.repository.AuthCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    private final FollowRepository followRepository;

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
            ViewTagRepository viewTagRepository, RefreshTokenService refreshTokenService, JwtTokenProvider jwtTokenProvider, FollowRepository followRepository) {
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
        this.followRepository = followRepository;
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 더미 유저 구분 조건 (예: nickname이 knk_06X로 시작하면 더미라고 가정)
        if (user.getNickname().startsWith("knk_")) {
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

    // 회원가입용 검사
    @Transactional
    public void registerUser(AddUserRequest request) {
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

    @Transactional
    public void deleteUser(Long id) {
        // 1. Board 삭제
        boardService.deleteBoardsByUser(id);

        // 2. Follow 삭제
        followService.deleteFollowByUser(id);

        // 3. Notification 삭제
        notificationService.deleteNotificationsByUser(id);

        // 4. Story 삭제
        storyService.deleteStoriesByUser(id);

        // 5. ChatUser 삭제
        chatUserService.deleteChatUsersByUser(id);

        // 6. 마지막으로 User 삭제
        userRepository.deleteById(id);
    }

    // 비밀번호 수정 메서드 - 민감한 정보니 프로필 수정과 분리
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

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public List<FollowRecommendResponse> searchUsersByNickname(String keyword) {
        Long currentUserId = SecurityUtil.getCurrentUserId();        List<User> users = userRepository.findTop20ByNicknameContainingIgnoreCase(keyword);

        return users.stream()
                .filter(user -> !user.getId().equals(currentUserId))  // ✅ 본인 제외
                .map(user -> {
                    boolean isStory = storyService.isStory(user.getId());
                    return FollowRecommendResponse.from(user,isStory); // ✅ user + boardCount 같이 넘김
                })
                .toList();
    }

    public List<User> findByPhone(String phone) {
        return userRepository.findAllByPhone(phone);
    }

    public void updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public String findUsernameByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
                .getUsername();
    }

    public void updateReadAlarmId(Long id, Long readAlarmId) {
        User user = getUserById(id);   // 기존 메서드 사용해서 유저 찾고
        user.updateReadAlarmId(readAlarmId); // 새 필드 업데이트
        userRepository.save(user);    // 저장
    }

    public void updateReadBoardId(Long id, Long readBoardId) {
        User user = getUserById(id);
        user.updateReadBoardId(readBoardId);
        userRepository.save(user);
    }

    // 마지막 읽은 게시글 id 가져오기
    public Long getReadBoardId(Long id) {
        User user = getUserById(id);
        return user.getReadBoardId();
    }

    // 마지막 읽은 알람 id 가져오기
    public Long getReadAlarmId(Long id) {
        User user = getUserById(id);
        return user.getReadAlarmId();
    }

    // 상위 20개 태그 가져오기
    public List<ViewTag> getTop20ViewTags(Long userId) {
        return viewTagRepository.findTop20ByUserIdOrderByViewsDesc(userId);
    }

    // 업데이트 소개글
    public void updateContent(Long id, String content) {
        User user = getUserById(id);
        user.updateContent(content);
        userRepository.save(user);
    }

    // 업데이트 이미지
    public void updateImg(Long id, String img) {
        User user = getUserById(id);
        user.updateImg(img);
        userRepository.save(user);
    }

    // 업데이트 구매등급
    public void updateRate(Long userId, Integer rate) {
        User user = getUserById(userId);
        user.updateRate(rate);
    }

    // 소셜로그인 최초 이용자 추가 정보 입력
    @Transactional
    public void completeSocialInfo(Long userId, SocialInfoRequest request) {
        User user = getUserById(userId); // ✅ 기존 메서드 재사용

        // 닉네임 중복 검사
        if (request.getNickname() != null && userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 추가 정보 저장
        user.setNickname(request.getNickname());
        user.setPhone(request.getPhone());
        userRepository.save(user);
    }

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

    @Transactional
    public AccessTokenResponse refreshAccessToken(RefreshTokenRequest request) {
        // 1. refreshToken 존재 여부 확인
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        // 2. refreshToken 만료 여부 검증
        if (!refreshTokenService.validateExpiration(refreshToken)) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // 3. refreshToken에 연결된 유저 정보 찾기
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 4. 새 accessToken 발급
        String newAccessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());

        // 5. 추가 정보 (최초 로그인 여부, 게시글 수)
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        int boardCount = boardService.countBoardsByUserId(user.getId());

        // 6. 결과 반환
        return new AccessTokenResponse(newAccessToken);
    }

    public boolean isFollowing(Long currentUserId, Long targetUserId) {
        return followRepository.existsByFollowerUserIdAndFollowingUserId(currentUserId, targetUserId);
    }




}




