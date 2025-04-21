
package com.glim.common.jwt.refresh.repository;

import com.glim.common.jwt.refresh.domain.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
    void deleteByToken(String token);         // ✅ 개별 로그아웃 용


}
