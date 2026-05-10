package tn.esprit.social_interaction_service.Reporting_Analytics.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COURSE_QUEUE = "course.queue";
    public static final String CHALLENGE_QUEUE = "challenge.queue";
    public static final String CERTIFICATION_QUEUE = "certification.queue";
    public static final String EVENT_QUEUE = "event.queue";
    public static final String BOOK_QUEUE = "book.queue";
    public static final String CLUB_QUEUE = "club.queue";

    @Bean
    public Queue courseQueue() {
        return new Queue(COURSE_QUEUE, true);
    }

    @Bean
    public Queue challengeQueue() {
        return new Queue(CHALLENGE_QUEUE, true);
    }

    @Bean
    public Queue certificationQueue() {
        return new Queue(CERTIFICATION_QUEUE, true);
    }

    @Bean
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE, true);
    }

    @Bean
    public Queue bookQueue() {
        return new Queue(BOOK_QUEUE, true);
    }

    @Bean
    public Queue clubQueue() {
        return new Queue(CLUB_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
