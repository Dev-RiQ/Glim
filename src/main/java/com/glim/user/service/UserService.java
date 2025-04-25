package com.glim.user.service;

import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.user.domain.User;
import com.glim.user.dto.request.AddUserRequest;
import com.glim.user.dto.request.ChangePasswordRequest;
import com.glim.user.dto.request.LoginRequest;
import com.glim.user.dto.request.UpdateUserRequest;
import com.glim.user.dto.response.UserResponse;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AwsS3Util awsS3Util;

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

        // ✅ 성별 수정
        if (request.getSex() != null) {
            user.setSex(request.getSex());
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user); // ✅ 완전 삭제!
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
                .map(UserResponse::from)
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


}




