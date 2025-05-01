
package com.glim.common.jwt.filter;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.jwt.provider.JwtTokenProvider;
import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.service.RefreshTokenService;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

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

        // 헤더에서 jwt 토큰 추출 ( access )
        Long userId = null;

        try {
            jwtTokenProvider.validateTokenOrThrow(token);

            userId = jwtTokenProvider.getUserId(token);
            setAuthenticationContext(userId);
            filterChain.doFilter(request, response);
            return;

        } catch (ExpiredJwtException e) {

            userId = jwtTokenProvider.getUserIdFromExpiredToken(e);
            System.out.println("userId = " + userId);
            // refreshToken 로직으로 넘어감

        } catch (JwtException e) {
        }


        if (userId != null) {
            Optional<RefreshToken> refreshOpt = refreshTokenService.findByUserId(userId);

            if (refreshOpt.isPresent() && refreshTokenService.validateExpiration(refreshOpt.get())) {
                // 3️⃣ refreshToken 유효 → 새 AccessToken 발급
                String newAccessToken = jwtTokenProvider.createToken(userId, "USER");

                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{ \"accessToken\": \"" + newAccessToken + "\" }");
                return;


            } else {
                // 4️⃣ refreshToken 만료 또는 없음 → DB에서 삭제 + 로그아웃 처리
                refreshOpt.ifPresent(tokenObj -> refreshTokenService.deleteByUserId(tokenObj.getUserId()));

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{ \"message\": \"로그아웃: refreshToken 만료됨\" }");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        SecurityUserDto userDetails = SecurityUserDto.of(user);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request()));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
