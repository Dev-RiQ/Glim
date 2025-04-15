package com.glim.common.security.service;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.user.domain.User;
import com.glim.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // ✅ 로컬 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return (UserDetails) convertToSecurityUserDto(user);

    }

    // ✅ 소셜 로그인 처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, naver
        String userNameAttribute = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttribute, oAuth2User.getAttributes());

   /*     User user = saveOrUpdate(attributes, registrationId);*/
/*
        return (OAuth2User) convertToSecurityUserDto(user, attributes.getAttributes());*/
        return null;
    }

    // ✅ 사용자 저장 or 업데이트
  /*  private User saveOrUpdate(OAuthAttributes attributes, String registrationId) {
        return userRepository.findByUsername(attributes.getEmail())
                .orElseGet(() -> userRepository.save(attributes.toEntity(registrationId)));
    }*/

    // ✅ 로컬, 소셜 공통 SecurityUserDto 반환
    private SecurityUserDto convertToSecurityUserDto(User user) {
        return SecurityUserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .img(user.getImg())
                .role(user.getRole())
                .build();
    }

    // ✅ 소셜 로그인 시에도 동일하게 SecurityUserDto 사용
    private SecurityUserDto convertToSecurityUserDto(User user, Map<String, Object> attributes) {
        return convertToSecurityUserDto(user); // 필요한 경우 attributes 포함한 커스텀 OAuth2User 만들어도 됨
    }
}
