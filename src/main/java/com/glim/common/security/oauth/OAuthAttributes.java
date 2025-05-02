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
    private final String phone;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String username, String name, String email, String img, String phone) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.name = name;
        this.email = email;
        this.img = img;
        this.phone = phone;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", (Map<String, Object>) attributes.get("response"));
        } else if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .username((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .phone("") // 구글은 전화번호 없음
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> response) {
        return OAuthAttributes.builder()
                .username((String) response.get("email"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .phone((String) response.get("mobile"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .username((String) kakaoAccount.get("email"))
                .name(profile != null ? (String) profile.get("nickname") : "카카오사용자")
                .email((String) kakaoAccount.get("email"))
                .phone("") // 카카오는 전화번호 따로 받아야 함
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(String registrationId) {
        return User.builder()
                .username(username)
                .name(name)
                .nickname(null)
                .img(img != null ? img : "userimages/user-default-image")
                .role(Role.ROLE_USER)
                .platForm(PlatForm.valueOf(registrationId.toUpperCase()))
                .phone(phone != null ? phone : "")
                .build();
    }
}
