
package com.glim.user.service;

import com.glim.user.domain.ResetToken;
import com.glim.user.repository.ResetTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetTokenService {

    private final ResetTokenRepository tokenRepository;
    @Transactional
    public String createResetToken(String username) {
        String token = UUID.randomUUID().toString();

        ResetToken resetToken = ResetToken.builder()
                .token(token)
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();

        tokenRepository.save(resetToken);
        return token;
    }

    public Optional<String> getUsernameByToken(String token) {
        return tokenRepository.findById(token).map(ResetToken::getUsername);
    }
    @Transactional
    public void deleteToken(String token) {
        tokenRepository.deleteById(token);
    }
    public Optional<ResetToken> getTokenEntity(String token) {
        return tokenRepository.findById(token);
    }

}
