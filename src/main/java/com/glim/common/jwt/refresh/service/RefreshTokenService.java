
package com.glim.common.jwt.refresh.service;

import com.glim.common.jwt.refresh.domain.RefreshToken;
import com.glim.common.jwt.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration-ms:604800000}") // 7일 기본값
    private long refreshTokenDurationMs;

    public RefreshToken createRefreshToken(Long userId) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusMillis(refreshTokenDurationMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiryDate(expiry)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public boolean validateExpiration(RefreshToken token) {
        return token.getExpiryDate().isAfter(Instant.now());
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public void validateOwnership(Long userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("해당 토큰 없음"));

        if (!refreshToken.getUserId().equals(userId)) {
            throw new SecurityException("본인의 토큰만 삭제할 수 있습니다.");
        }
    }
}
