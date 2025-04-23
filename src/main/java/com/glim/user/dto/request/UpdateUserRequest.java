package com.glim.user.dto.request;

import com.glim.user.domain.Sex;
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
    private String content;
    private Sex sex;
}
