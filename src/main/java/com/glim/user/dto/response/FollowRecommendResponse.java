package com.glim.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glim.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRecommendResponse {
    private Long userId;
    private String nickname;
    private String img;
    private String name;
    private Boolean isStory;

    // ✅ 정적 팩토리 메서드 추가
    public static FollowRecommendResponse from(User user, boolean story) {
        return new FollowRecommendResponse(
                user.getId(),
                user.getNickname(),
                user.getImg(),
                user.getName(),
                story
        );
    }
}




