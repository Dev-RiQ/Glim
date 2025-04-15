package com.glim.chating.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateChatUserRequest {
    private Long roomId;
    private Long userId;
}
