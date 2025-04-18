package com.glim.user.controller;

import com.glim.common.jwt.JwtTokenProvider;
import com.glim.user.domain.User;
import com.glim.user.dto.request.AddUserRequest;
import com.glim.user.dto.request.ChangePasswordRequest;
import com.glim.user.dto.request.LoginRequest;
import com.glim.user.dto.request.UpdateUserRequest;
import com.glim.user.dto.response.LoginResponse;
import com.glim.user.dto.response.UserResponse;
import com.glim.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //    @PostMapping("/login")
//    public ResponseEntity<SecurityUserDto> login(@RequestBody LoginRequest request) {
//        System.out.println("login api 호출완료");
//        SecurityUserDto user = userService.login(request);
//        return ResponseEntity.ok(user);
//    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request);
        String token = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        UserResponse userResponse = UserResponse.from(user);
        return ResponseEntity.ok(new LoginResponse(token, userResponse));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequest request) {
        System.out.println("sign up api 호출 완료");
        userService.registerUser(request);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        userService.updateUser(id, request);
        return ResponseEntity.ok("회원정보 수정 완료");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(id, request);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }



}
