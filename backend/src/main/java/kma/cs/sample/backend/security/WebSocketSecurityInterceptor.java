package kma.cs.sample.backend.security;

import static io.jsonwebtoken.lang.Strings.hasText;
import static kma.cs.sample.backend.security.InitialJwtAuthenticationFilter.AUTHORIZATION_HEADER;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.messaging.util.matcher.MessageMatcher;
import org.springframework.security.messaging.util.matcher.OrMessageMatcher;
import org.springframework.security.messaging.util.matcher.SimpMessageTypeMatcher;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import kma.cs.sample.backend.domain.AuthenticatedUser;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private final static MessageMatcher<Object> SECURED_MESSAGES
        = new OrMessageMatcher<>(new SimpMessageTypeMatcher(SimpMessageType.MESSAGE), new SimpMessageTypeMatcher(SimpMessageType.SUBSCRIBE));

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        if (!SECURED_MESSAGES.matches(message)) {
            return message;
        }

        final StompHeaderAccessor accessor = getMessageAccessor(message);

        final String token = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
        if (hasText(token)) {
            try {
                final String username = jwtTokenProvider.extractUserLoginFromToken(token);
                final AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetailsService.loadUserByUsername(username);

                final UsernamePasswordAuthenticationToken auth
                    = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);
            } catch (final ExpiredJwtException ex) {
                return null;
            }
        }

        return message;
    }

    private static StompHeaderAccessor getMessageAccessor(final Message<?> message) {
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }
}
