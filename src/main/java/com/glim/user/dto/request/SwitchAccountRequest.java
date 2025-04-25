package com.glim.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SwitchAccountRequest {
    private Long switchToUserId;
}
