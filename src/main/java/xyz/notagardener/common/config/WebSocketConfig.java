package xyz.notagardener.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${origin}")
    private String allowedOrigin;

    @Value("${app-origin}")
    private String allowedAppOrigin;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //stomp의 접속 주소
        registry.addEndpoint("/api/socket")
                .setAllowedOriginPatterns(allowedOrigin, allowedAppOrigin)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트의 send요청 처리
        registry.setApplicationDestinationPrefixes("/pub");
        // 클라이언트에게 메시지 전달
        registry.enableSimpleBroker("/queue", "/topic"); // 구독 경로 추가
    }
}
