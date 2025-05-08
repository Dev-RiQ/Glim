
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

    // refreshToken 생성 및 만료일 지정 , db에 저장
    public RefreshToken createRefreshToken(Long userId) {

        Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUserId(userId);

        if(existingTokenOpt.isPresent()) {
            RefreshToken existingToken = existingTokenOpt.get();
            return existingToken;
        }

        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusMillis(refreshTokenDurationMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiryDate(expiry)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    // 특정 토큰으로 db에서 조회
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // 특정 토큰 하나 삭제
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    // 유저 id로 모든 refreshToken 삭제
    public boolean validateExpiration(RefreshToken token) {
        return token.getExpiryDate().isAfter(Instant.now());
    }

    // refreshToken 만료 여부 체크
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    // 해당 토큰이 특정 유저의 것인지 확인
    public void validateOwnership(Long userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("해당 토큰 없음"));

        if (!refreshToken.getUserId().equals(userId)) {
            throw new SecurityException("본인의 토큰만 삭제할 수 있습니다.");
        }
    }
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }






}
