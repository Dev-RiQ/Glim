package com.glim.common.security.service;

import com.glim.common.exception.CustomException;
import com.glim.common.exception.ErrorCode;
import com.glim.common.security.dto.SecurityUserDto;
import com.glim.common.security.oauth.OAuthAttributes;
import com.glim.common.security.oauth.SecurityOAuth2User;
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

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // 로컬 로그인 처리
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return convertToSecurityUserDto(user);
    }

    // 소셜 로그인 처리
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, naver
//        String userNameAttribute = userRequest.getClientRegistration().getProviderDetails()
//                .getUserInfoEndpoint().getUserNameAttributeName();
//
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttribute, oAuth2User.getAttributes());
//        User user = saveOrUpdate(attributes, registrationId);
//
////        return new DefaultOAuth2User(
////                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
////                attributes.getAttributes(),
////                attributes.getNameAttributeKey()
////        );
//         return new SecurityOAuth2User(dto, attributes);
//    }
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            Map<String, Object> attributes = oAuth2User.getAttributes();
            OAuthAttributes authAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

            User user = saveOrUpdate(authAttributes, registrationId);

            SecurityUserDto dto = SecurityUserDto.of(user);
            return new SecurityOAuth2User(dto, attributes);
        }

    // 사용자 저장 or 업데이트
    private User saveOrUpdate(OAuthAttributes attributes, String registrationId) {
        return userRepository.findByUsername(attributes.getEmail())
                .orElseGet(() -> userRepository.save(attributes.toEntity(registrationId)));
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
