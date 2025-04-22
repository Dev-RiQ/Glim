package com.glim.user.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UpdateUserRequest {
    private String nickname;
    private String img;
    private String phone;
}
