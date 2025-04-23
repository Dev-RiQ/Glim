package com.glim.user.dto.response;
import com.glim.user.domain.Role;
import com.glim.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String nickname;
    private String img;
    private Role role;
    private Long followers;
    private Long followings;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .img(user.getImg())
                .role(user.getRole())
                .followers(user.getFollowers())
                .followings(user.getFollowings())
                .build();
    }
}