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

        String finalImg = null;
        String extractedFileName = null;

        if (rawImg != null && !rawImg.isBlank()) {
            if (rawImg.startsWith("http")) {
                finalImg = rawImg;
                extractedFileName = extractBaseFileName(rawImg);
            } else {
                finalImg = awsS3Util.getURL(rawImg, FileSize.IMAGE_128);
                extractedFileName = rawImg;
            }
        }

        return UserViewResponse.builder()
                .nickname(user.getNickname())
                .img(finalImg)                 // 정제된 URL
                .semiImg(extractedFileName)    // 잘라낸 파일명 또는 원본 파일명
                .name(user.getName())
                .content(user.getContent())
                .build();
    }

    private static String extractBaseFileName(String fullUrl) {
        if (fullUrl == null || fullUrl.isBlank()) {
            return null;
        }

        // 'glim-bucket/' 이후 경로 추출
        int bucketIndex = fullUrl.indexOf("glim-bucket/");
        if (bucketIndex == -1) {
            return fullUrl;  // 예상치 못한 URL이면 그대로 반환
        }
        String pathPart = fullUrl.substring(bucketIndex + "glim-bucket/".length());

        // 마지막 언더스코어(_) 이전까지만 (사이즈/확장자 제거)
        int underscoreIdx = pathPart.lastIndexOf("_");
        if (underscoreIdx != -1) {
            return pathPart.substring(0, underscoreIdx);
        }

        // 언더스코어 없으면 전체 반환
        return pathPart;
    }

}