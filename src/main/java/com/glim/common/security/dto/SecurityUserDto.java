package com.glim.common.security.dto;

import com.glim.user.domain.Role;
import com.glim.user.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Builder
public class SecurityUserDto implements UserDetails {
    private Long id;
    private String nickname;
    private String img;
    private Role role;
    private String name;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public static SecurityUserDto of(User user) {
        return SecurityUserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .name(user.getName())
                .img(user.getImg())
                .role(user.getRole())
                .build();
    }


    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

