package com.glim.user.dto.response;

import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserViewResponse {

    private String nickname;   // 닉네임
    private String img;        // 🔹 실제 S3 URL (정제된 128px 이미지)
    private String semiImg;    // 🔸 정제되지 않은 원본 파일명 그대로 (예: userimages/abc.jpg)
    private String name;       // 이름
    private String content;    // 소개글

    public static UserViewResponse from(User user, AwsS3Util awsS3Util) {
        String rawImg = user.getImg();

        return UserViewResponse.builder()
                .nickname(user.getNickname())
                .img(rawImg != null && !rawImg.isBlank()
                        ? awsS3Util.getURL(rawImg, FileSize.IMAGE_128) // 정제된 URL
                        : null)
                .semiImg(rawImg) // 정제 안 된 순수 파일명
                .name(user.getName())
                .content(user.getContent())
                .build();
    }
}
