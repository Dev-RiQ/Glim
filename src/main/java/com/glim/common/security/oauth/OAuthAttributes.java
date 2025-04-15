package com.glim.common.security.oauth;

import com.glim.user.domain.PlatForm;
import com.glim.user.domain.Role;
import com.glim.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String username;
    private final String name;
    private final String email;
    private final String img;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String username, String name, String email, String img) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.name = name;
        this.email = email;
        this.img = img;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", (Map<String, Object>) attributes.get("response"));
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .username((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .img((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> response) {
        return OAuthAttributes.builder()
                .username((String) response.get("email"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .img((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(String registrationId) {
        return User.builder()
                .username(email)
                .name(name != null ? name : "")
                .img(img != null ? img : "")
                .platForm(PlatForm.valueOf(registrationId.toUpperCase()))
                .role(Role.ROLE_USER)
                .build();
    }

}
