package com.glim.user.service;

import com.glim.borad.service.BoardService;
import com.glim.chating.service.ChatUserService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.notification.service.NotificationService;
import com.glim.stories.service.StoryService;
import com.glim.user.domain.User;
import com.glim.user.dto.request.AddUserRequest;
import com.glim.user.dto.request.ChangePasswordRequest;
import com.glim.user.dto.request.LoginRequest;
import com.glim.user.dto.request.UpdateUserRequest;
import com.glim.user.dto.response.UserResponse;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AwsS3Util awsS3Util,
            BoardService boardService,
            @Lazy FollowService followService,
            @Lazy NotificationService notificationService,
            StoryService storyService,
            ChatUserService chatUserService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.awsS3Util = awsS3Util;
        this.boardService = boardService;
        this.followService = followService;
        this.notificationService = notificationService;
        this.storyService = storyService;
        this.chatUserService = chatUserService;
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return user;
    }


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

        // ✅ 전화번호 중복 검사
        if (request.getPhone() != null &&
                !user.getPhone().equals(request.getPhone()) &&
                userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
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

        // ✅ 전화번호 수정
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
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

    public List<UserResponse> searchUsersByNickname(String keyword) {
        List<User> users = userRepository.findTop20ByNicknameContainingIgnoreCase(keyword);

        return users.stream()
                .map(user -> {
                    int boardCount = boardService.countBoardsByUserId(user.getId()); // ✅ user별 게시글 수 조회
                    return UserResponse.from(user, boardCount); // ✅ user + boardCount 같이 넘김
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

    public Long getReadBoardId(Long id) {
        User user = getUserById(id);
        return user.getReadBoardId();
    }

    public Long getReadAlarmId(Long id) {
        User user = getUserById(id);
        return user.getReadAlarmId();
    }
    public void updateContent(Long id, String content) {
        User user = getUserById(id);
        user.updateContent(content);
        userRepository.save(user);
    }
    public void updateImg(Long id, String img) {
        User user = getUserById(id);
        user.updateImg(img);
        userRepository.save(user);
    }
    public void updateRate(Long userId, Integer rate) {
        User user = getUserById(userId);
        user.updateRate(rate);
    }








}




