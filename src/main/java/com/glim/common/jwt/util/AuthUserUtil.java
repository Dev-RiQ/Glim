package com.glim.common.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.user.domain.User;
import com.glim.user.dto.response.LoginResponse;
import com.glim.user.dto.response.UserResponse;
import com.glim.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthUserUtil {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    public User getUserFromToken(String token) {
        Long userId = jwtTokenProvider.getUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    //    public void writeLoginResponse(HttpServletResponse response, User user, String accessToken) throws IOException {
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
//        LoginResponse loginResponse = new LoginResponse(
//                accessToken,
//                refreshToken.getToken(),
//                UserResponse.from(user)
//        );
    public void writeLoginResponse(HttpServletResponse response, User user, String accessToken) throws IOException {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);

        LoginResponse loginResponse = new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                UserResponse.from(user),
                isFirstLogin
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }
}


