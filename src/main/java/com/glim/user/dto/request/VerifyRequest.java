package com.glim.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequest {
    private String phone;
    private String code;
}
