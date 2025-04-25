
package com.glim.user.dto.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String resetToken;
    private String username;
    private String newPassword;
}
