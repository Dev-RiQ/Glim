package com.glim.common.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

//    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() { // 스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/static/**"),
                        new AntPathRequestMatcher("/img/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**")
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((auth) -> auth
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/login")
//                        .userInfoEndpoint((userInfoEndpointConfig) ->
//                                userInfoEndpointConfig.userService(customOAuth2UserService))
                )
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"));

        http
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers("/","/oauth","/oauth2/**", "/login","/login?error", "/sign-up", "/dummy", "/dummy/*").permitAll()
                            .requestMatchers("/chat","/chat/**","/pub","/pub/**","/sub","/sub/**").permitAll()
                            .anyRequest().authenticated();
                });




//        //원규
//        http
//                .authorizeHttpRequests((auth) -> {
//                    auth
//                            .requestMatchers("/").permitAll();
//                });
//
//
//
//
//
//
//        //종석
//        http
//                .authorizeHttpRequests((auth) -> {
//                    auth
//                            .requestMatchers("/").permitAll();
//                });
//
//
//
//
//
//        //나경
//        http
//                .authorizeHttpRequests((auth) -> {
//                    auth
//                            .requestMatchers("/").permitAll();
//                });

        return http.build();
    }
}