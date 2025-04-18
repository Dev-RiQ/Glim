package com.glim.user.dto.response;

import com.glim.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginResponse {
    private String accessToken;
    private boolean isNew;
    private UserResponse user;

    public static SocialLoginResponse of(String token, boolean isNew, User user) {
        return SocialLoginResponse.builder()
                .accessToken(token)
                .isNew(isNew)
                .user(UserResponse.from(user))
                .build();
    }
}