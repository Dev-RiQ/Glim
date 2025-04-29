package com.glim.common.kafka.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // WebSocket을 활성화하고 메시지 브로커 사용가능
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 엔드포인트를 등록하는 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-socket") // 엔드포인트 설정
                .setAllowedOriginPatterns("*") // 모든 Origin 허용 -> 배포시에는 보안을 위해 Origin을 정확히 지정
                .withSockJS(); // SockJS 사용가능 설정
    }

    // 메시지 브로커를 구성하는 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // /sub/{chatNo}로 주제 구독 가능
        registry.setApplicationDestinationPrefixes("/pub"); // /pub/message로 메시지 전송 컨트롤러 라우팅 가능
    }

    // 64KB 이상의 데이터 전송을 못하는 문제 해결
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(160 * 64 * 1024);
        registry.setSendTimeLimit(100 * 10000);
        registry.setSendBufferSizeLimit(3 * 512 * 1024);
    }

}