package tn.esprit.employee.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;

@Component
@Slf4j
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> nativeHeaders = accessor.getNativeHeader("token");
            String token = (nativeHeaders != null && !nativeHeaders.isEmpty()) ? nativeHeaders.get(0) : null;
            
            if (token != null) {
                try {
                    // Modern 0.12.x API matching DevUnity
                    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

                    Claims claims = Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

                    String username = claims.getSubject();
                    
                    if (username != null) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, List.of());
                        accessor.setUser(auth);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.info("[WS-Security] Authenticated user: {}", username);
                    }
                } catch (Exception e) {
                    log.error("[WS-Security] JWT validation failed: {}", e.getMessage());
                }
            }
        }
        return message;
    }
}
