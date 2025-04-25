package com.glim.verification.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneVerifyRequest {
    private String phone;
    private String code;
}
