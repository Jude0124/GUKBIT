package com.gukbit.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS(); // 클라이언트가 웹소켓 서버에 연결하는데 사용할 엔드포인트를 등록
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // "/app"으로 시작하는 메시지가 메세지를 처리하는 메서드(message-handling methods)로 라우팅 되어야 한다고 정의
        registry.enableSimpleBroker("/topic"); // "/topic"으로 시작하는 메시지가 메세지 브로커로 라우팅 되어야 한다고 정의
    }
}
