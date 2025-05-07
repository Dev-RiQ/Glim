package com.glim.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialInfoRequest {
    private String nickname;
    private String phone;
    private String accessToken;
}
