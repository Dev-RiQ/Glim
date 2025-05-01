package com.glim.user.dto.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
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
    @JsonProperty("isStory")
    private boolean story;

    public static UserResponse from(User user, int boardCount, boolean story, String img) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .img(img)
                .followers(user.getFollowers())
                .followings(user.getFollowings())
                .content(user.getContent())
                .name(user.getName())
                .rate(user.getRate())
                .boardCount(boardCount)
                .story(story)
                .build();
    }
}