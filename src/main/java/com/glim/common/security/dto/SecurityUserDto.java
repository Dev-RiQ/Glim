package com.glim.common.security.dto;

import com.glim.user.domain.Role;
import lombok.*;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Builder
public class SecurityUserDto {
    private String username;
    private String nickname;
    private String img;
    private Role role;
}
