package com.glim.user.service;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.user.domain.User;
import com.glim.user.dto.request.AddUserRequest;
import com.glim.user.dto.request.ChangePasswordRequest;
import com.glim.user.dto.request.LoginRequest;
import com.glim.user.dto.request.UpdateUserRequest;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 로컬 로그인 처리
//    public SecurityUserDto login(LoginRequest request) {
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new CustomException(ErrorCode.INVALID_PASSWORD);
//        }
//
//
//        return SecurityUserDto.builder()
//                .id(user.getId())
//                .nickname(user.getNickname())
//                .img(user.getImg())
//                .role(user.getRole())
//                .build();
//    }

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

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
        }

        User user = request.toEntity();
        user.encodePassword(passwordEncoder); // 비밀번호 암호화
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, UpdateUserRequest request) {
        // 🔍 유저 ID로 사용자 조회 (없으면 예외)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // ✅ 닉네임 중복 검사
        // 입력된 닉네임이 null이 아니고, 기존 닉네임과 다르며 DB에 이미 존재하면 예외
        if (request.getNickname() != null &&
                !user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        //  전화번호 중복 검사
        if (request.getPhone() != null &&
                !user.getPhone().equals(request.getPhone()) &&
                userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
        }

        //  닉네임 수정 (값이 있을 때만)
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        //  이미지 URL 수정
        if (request.getImg() != null) {
            user.setImg(request.getImg());
        }

        //  전화번호 수정
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
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





}
