package com.glim.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glim.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowUserResponse { // 팔로워, 팔로잉 목록 응답
    private String nickname;
    private String img;
    private Long userId;
    private String name;
    @JsonProperty("isStory")
    private boolean story;

    public static FollowUserResponse from(User user, boolean story) {
        return new FollowUserResponse(
                user.getNickname(),
                user.getImg(),
                user.getId(),
                user.getName(),
                story
        );
    }
}
