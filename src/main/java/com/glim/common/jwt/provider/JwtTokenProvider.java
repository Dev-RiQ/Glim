package com.glim.common.jwt.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider { // accessToken 발급, 검증

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expire-length}")
    private long expireLength;

    private Key key;

    @PostConstruct
    protected void init() {
        log.debug("⚠️  [DEBUG] 읽어온 secretKey: {}", secretKey);
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(Long userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        claims.put("role", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject());
    }

    public void validateTokenOrThrow(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) // ✅ 시크릿키 설정
                .build()
                .parseClaimsJws(token)
                .getBody(); // ✅ JWT 안에 있는 본문(Claims) 꺼내기

        return Long.valueOf(claims.getSubject()); // subject에 userId가 들어있다고 가정
    }

    public Long getUserIdFromExpiredToken(ExpiredJwtException e) {
        return Long.parseLong(e.getClaims().getSubject());
    }
}
