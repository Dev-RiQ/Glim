package com.glim.common.jwt.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glim.borad.service.BoardService;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.stories.service.StoryService;
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
    private final BoardService boardService;
    private final StoryService storyService;
    private final AwsS3Util awsS3Util;

    public User getUserFromToken(String token) {
        Long userId = jwtTokenProvider.getUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public void writeLoginResponse(HttpServletResponse response, User user, String accessToken) throws IOException {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        boolean isFirstLogin = (user.getNickname() == null || user.getPhone() == null);
        int boardCount = boardService.countBoardsByUserId(user.getId());
        boolean isStory = storyService.isStory(user.getId());
        String img = awsS3Util.getURL(user.getImg(), FileSize.IMAGE_128);

        LoginResponse loginResponse = new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                UserResponse.from(user, boardCount, isStory, img),
                isFirstLogin
        );

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
    }
}


