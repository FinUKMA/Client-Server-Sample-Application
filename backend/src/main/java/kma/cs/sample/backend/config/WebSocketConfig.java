package kma.cs.sample.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import kma.cs.sample.backend.security.WebSocketSecurityInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    private final WebSocketSecurityInterceptor webSocketSecurityInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/execute").setAllowedOrigins("*");
    }

    @Override
    protected void configureInbound(final MessageSecurityMetadataSourceRegistry messages) {
        messages
            .simpDestMatchers("/execute").authenticated()
            .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll();
    }

    @Override
    protected void customizeClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(webSocketSecurityInterceptor);
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}