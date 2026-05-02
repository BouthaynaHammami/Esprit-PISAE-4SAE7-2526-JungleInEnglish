package tn.esprit.language_courses_service.ChallengesCompetitions.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class ChallengeWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enregistre les préfixes pour les messages sortants (broadcast)
        config.enableSimpleBroker("/topic");
        // Enregistre le préfixe pour les messages entrants (destination @MessageMapping)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Définit le point d'entrée pour la connexion au WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // À restreindre en prod
                .withSockJS();
    }
}
