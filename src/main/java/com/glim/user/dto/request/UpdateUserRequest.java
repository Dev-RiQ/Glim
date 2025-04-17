package com.glim.user.dto.request;

import lombok.*;

@NoArgsConstructor         // 기본 생성자
@AllArgsConstructor        // 모든 필드 생성자
@ToString
@Getter
@Setter
public class UpdateUserRequest {
    private String nickname;
    private String img;
    private String phone;
}
