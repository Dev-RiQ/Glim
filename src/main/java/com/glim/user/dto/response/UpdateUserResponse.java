package com.glim.user.dto.response;

import com.glim.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UpdateUserResponse {
    private String nickname; // 닉네임
    private String img;       // 이미지 링크
    private String name;      // 이름 (이름을 user 엔티티에 따로 저장하고 있어야 해)
    private String content;   // 자기소개 or 콘텐츠 내용
    private boolean isMe;
    private boolean isFollowing;

    public static UpdateUserResponse from(User target, Long currentUserId, boolean isFollowing) {
        return UpdateUserResponse.builder()
                .nickname(target.getNickname())
                .img(target.getImg())
                .name(target.getName())
                .content(target.getContent())
                .isMe(target.getId().equals(currentUserId))   // 본인 여부 체크
                .isFollowing(isFollowing)                     // 팔로우 여부는 서비스에서 결정
                .build();
    }
}
