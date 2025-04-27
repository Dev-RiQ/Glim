package com.glim.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindUsernameRequest {

    private String phone;   // 전화번호
    private String code;    // 인증번호
}
