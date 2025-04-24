package com.glim.user.dto.request;

import com.glim.common.security.oauth.OAuthAttributes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthLoginRequest {
    private OAuthAttributes attributes; // 소셜에서 받아온 사용자 정보
    private String provider; // "google", "naver", "kakao"
}

