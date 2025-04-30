package com.glim.user.dto.response;
import com.glim.user.domain.Role;
import com.glim.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String nickname;
    private String img;
    private Long followers;
    private Long followings;
    private String content;
    private String name;
    private Integer rate;
    private Integer boardCount;

    public static UserResponse from(User user, int boardCount) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .img(user.getImg())
                .followers(user.getFollowers())
                .followings(user.getFollowings())
                .content(user.getContent())
                .name(user.getName())
                .rate(user.getRate())
                .boardCount(boardCount)
                .build();
    }
}