package com.glim.common.security.dto;

import com.glim.user.domain.Role;
import lombok.*;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Builder
public class SecurityUserDto {
    private Long id;
    private String nickname;
    private String img;
    private Role role;
}
