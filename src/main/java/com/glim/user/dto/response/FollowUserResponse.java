package com.glim.user.dto.response;

import com.glim.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowUserResponse { // 팔로워, 팔로잉 목록 응답
    private String nickname;
    private String img;

    public static FollowUserResponse from(User user) {
        return new FollowUserResponse(user.getNickname(), user.getImg());
    }
}
