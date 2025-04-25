package com.glim.verification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerificationResultResponse {
    private boolean verified;
    private String message;
}
