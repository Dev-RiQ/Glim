package com.glim.common.security.oauth;

import com.glim.user.domain.PlatForm;
import com.glim.user.domain.Role;
import com.glim.user.domain.Sex;
import com.glim.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String username;
    private final String name;
    private final String email;
    private final String img;
    private final String sex;
    private final String birthday;
    private final String birthyear;
    private final String phone;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String username, String name, String email, String img, String sex, String birthday, String birthyear, String phone) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.name = name;
        this.email = email;
        this.img = img;
        this.sex = sex;
        this.birthday = birthday;
        this.birthyear = birthyear;
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

    // google에서 받아온 정보 처리
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

    // naver에서 받아온 정보 처리
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> response) {
        String email = (String) response.get("email");                   // 네이버 이메일
        String name = (String) response.get("name");                     // 이름 (10자 이내)
        String nickname = (String) response.get("nickname");             // 별명
        String img = (String) response.get("profile_image");             // 프로필 이미지
        String gender = (String) response.get("gender");                 // 성별: "M" or "F"
        String birthday = (String) response.get("birthday");             // 생일: "MM-DD"
        String birthyear = (String) response.get("birthyear");           // 출생연도: "YYYY"
        String mobile = (String) response.get("mobile");                 // 휴대전화번호: 010-0000-0000


        return OAuthAttributes.builder()
                .username(email)                      // username 기준은 이메일
                .name(name != null ? name : nickname) // 이름 없으면 닉네임 사용
                .email(email)
                .img(img)
                .sex(gender)
                .birthday(birthday)
                .birthyear(birthyear)
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .phone(mobile)
                .build();
    }

    // kakako에서 받아온 정보 처리
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String email = (String) kakaoAccount.get("email");
        String gender = (String) kakaoAccount.get("gender");
        String birthday = (String) kakaoAccount.get("birthday");

        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .attributes(attributes)
                .email(email)
                .sex(gender)
                .birthday(birthday)
                .build();
    }

    public User toEntity(String registrationId) {
        return User.builder()
                .username(username)
                .name(name)
                .nickname(null)  // nickname은 받아오는게 아니므로 null로 처리
                .img(img != null ? img : "")
                .sex(sex != null ? (convertToSexEnum(sex)) : Sex.UNKNOWN)
                .birth(toLocalDate(birthyear, birthday))
                .role(Role.ROLE_USER)
                .platForm(PlatForm.valueOf(registrationId.toUpperCase())) // 실제 registrationId로 바꿔서 처리해도 됨
                .phone(phone != null ? phone : "")
                .build();
    }

    // ✅ 문자열 성별을 Sex Enum으로 변환하는 유틸 함수
    private Sex convertToSexEnum(String gender) {
        if (gender == null) return Sex.UNKNOWN;
        return switch (gender.toLowerCase()) {
            case "M", "남성", "m" -> Sex.MALE;
            case "F", "여성", "f" -> Sex.FEMALE;
            default -> Sex.UNKNOWN;
        };
    }

    // db에 맞는 birth 파싱
    private LocalDate toLocalDate(String year, String day) {
        try {
            if (year == null || day == null) return null;

            // 생일 형식 MM-DD or MMDD → 파싱 처리
            String month = "", date = "";

            if (day.contains("-")) { // ex) 08-15
                String[] parts = day.split("-");
                month = parts[0];
                date = parts[1];
            } else if (day.length() == 4) { // ex) 0815
                month = day.substring(0, 2);
                date = day.substring(2, 4);
            } else {
                return null;
            }

            return LocalDate.parse(year + "-" + month + "-" + date);
        } catch (Exception e) {
            return null; // 파싱 실패 시 null
        }
    }
}
