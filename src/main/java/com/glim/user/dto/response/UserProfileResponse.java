package com.glim.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glim.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {

    private Long id;
    private String nickname;
    private String img;
    private Long followers;
    private Long followings;
    private String content;
    private String name;
    private Integer rate;
    private Integer boardCount;
    private Boolean isStory;
    private Boolean isMine;        // ✅ 내가 조회한 내 프로필인지 여부
    private Boolean isFollowing;   // ✅ 내가 이 유저를 팔로우 중인지 여부

    public static UserProfileResponse from(Long currentUserId, User target, int boardCount, boolean isFollowing ,boolean isStory, String img) {
        return UserProfileResponse.builder()
                .id(target.getId())
                .nickname(target.getNickname())
                .img(img)
                .followers(target.getFollowers())
                .followings(target.getFollowings())
                .content(target.getContent())
                .name(target.getName())
                .rate(target.getRate())
                .boardCount(boardCount)
                .isMine(target.getId().equals(currentUserId))  // ✅ isMine 계산
                .isFollowing(isFollowing)
                .isStory(isStory)
                .build();
    }
}
