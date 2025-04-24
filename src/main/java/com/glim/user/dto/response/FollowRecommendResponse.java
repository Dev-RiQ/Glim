package com.glim.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRecommendResponse {
    private Long userId;
    private String nickname;
    private String img;
}
