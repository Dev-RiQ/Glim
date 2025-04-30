package com.glim.common.security.service;

import com.glim.common.awsS3.domain.FileSize;
import com.glim.common.awsS3.service.AwsS3Util;
import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.oauth.SecurityOAuth2User;
import com.glim.user.domain.PlatForm;
import com.glim.user.domain.Role;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final AwsS3Util awsS3Util;

    // 로컬 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return convertToSecurityUserDto(user);
    }

    // 소셜 로그인 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Spring 기본 OAuth2 유저 서비스 이용
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        // 구글 & 네이버 에서 유저 정보 받아오기
        OAuth2User oAuth2User;/*= delegate.loadUser(userRequest);*/
        try {
            oAuth2User = delegate.loadUser(userRequest);
        } catch (OAuth2AuthenticationException e) {
            System.out.println("❌ OAuth2AuthenticationException 발생!");
            e.printStackTrace();
            throw e;
        }

        // 어떤 플랫폼인지 확인 ( naver, google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 유저를 식별할 키 받기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 확인작업
        System.out.println("=================================================");
        System.out.println("🌈 registrationId: " + registrationId);
        System.out.println("🔥 attributes: " + attributes);
        System.out.println("=================================================");

        OAuthAttributes authAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        User user = saveOrUpdate(authAttributes, registrationId);

        SecurityUserDto dto = SecurityUserDto.of(user);
        return new SecurityOAuth2User(dto, attributes);
    }

    public User saveOrUpdate(OAuthAttributes attributes, String registrationId) {
        Optional<User> userOptional = userRepository.findByUsername(attributes.getEmail());

        if (userOptional.isPresent()) {
            return userOptional.get(); // 기존 유저 → 그대로 로그인
        }

        // ✅ img가 null이거나 빈 문자열이면 기본 이미지 경로로 설정
        String rawImg = "userimages/user-default-image";

        String finalImg = awsS3Util.getURL(rawImg, FileSize.IMAGE_128);

        // 새 유저 등록 (처음 소셜 로그인)
        User user = User.builder()
                .username(attributes.getEmail())
                .name(attributes.getName())
                .nickname(null) // 👉 nickname은 입력받을 예정
                .phone(null)    // 👉 phone도 본인인증 후 입력받을 예정
                .img(finalImg)
                .role(Role.ROLE_USER)
                .followers(0L)
                .followings(0L)
                .platForm(PlatForm.valueOf(registrationId.toUpperCase())) // ✅ enum 변환 명시적으로 처리
                .build();

        return userRepository.save(user);
    }


    // 로컬, 소셜 공통 SecurityUserDto 반환
    private SecurityUserDto convertToSecurityUserDto(User user) {
        return SecurityUserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .img(user.getImg())
                .role(user.getRole())
                .build();
    }
}
