
package com.glim.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordRequest {
    private String phone;
    private String code;
    private String username;
}
