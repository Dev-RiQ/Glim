package com.glim.common.security.handler;

import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.util.AuthUserUtil;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.SecurityOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthUserUtil authUserUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityOAuth2User oauth2User = (SecurityOAuth2User) authentication.getPrincipal();
        SecurityUserDto securityUserDto = oauth2User.getUserDto();

        // 사용자 정보 가져오기
        User user = userRepository.findById(securityUserDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // accessToken 생성
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getRole().name());
        System.out.println("Dddddddddd");
        // accessToken + refreshToken + user 응답 내려주기
        authUserUtil.writeLoginResponse(response, user, accessToken);
        response.sendRedirect("http://localhost:3000/login/" + accessToken);
    }
}
