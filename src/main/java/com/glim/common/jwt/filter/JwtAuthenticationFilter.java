
package com.glim.common.jwt.filter;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        Long userId = null;

        try {
            // 유효한 AccessToken이면 그대로 인증 처리
            if (jwtTokenProvider.validateToken(token)) {
                userId = jwtTokenProvider.getUserId(token);
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // AccessToken이 만료된 경우 → RefreshToken으로 대체
            String refreshToken = request.getHeader("X-Refresh-Token");

            if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                userId = jwtTokenProvider.getUserId(refreshToken);

                // 새 AccessToken 생성
                String newAccessToken = jwtTokenProvider.createToken(userId, "USER"); // 또는 Role

                // 응답 헤더에 새 accessToken 추가
//                response.setHeader("Authorization", "Bearer " + newAccessToken);
                // ✅ JSON 응답으로 accessToken 내려주기
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);

                String json = "{ \"accessToken\": \"" + newAccessToken + "\" }";
                response.getWriter().write(json);

                return;
            } else {
                // RefreshToken 없거나 유효하지 않음
                filterChain.doFilter(request, response);
                return;
            }
        }

        // userId가 유효한 경우만 인증 객체 설정
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            SecurityUserDto securityUserDto = SecurityUserDto.of(user);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            securityUserDto, null, securityUserDto.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}