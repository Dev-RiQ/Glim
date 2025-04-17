
package com.glim.common.security.oauth;

import com.glim.common.security.dto.SecurityUserDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class SecurityOAuth2User implements OAuth2User {

    private final SecurityUserDto securityUserDto;
    private final Map<String, Object> attributes;

    public SecurityOAuth2User(SecurityUserDto securityUserDto, Map<String, Object> attributes) {
        this.securityUserDto = securityUserDto;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return securityUserDto.getAuthorities();
    }

    @Override
    public String getName() {
        return String.valueOf(securityUserDto.getId());
    }

    public SecurityUserDto getUserDto() {
        return securityUserDto;
    }
}
