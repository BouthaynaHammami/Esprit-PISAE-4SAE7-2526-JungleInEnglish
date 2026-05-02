package tn.esprit.language_courses_service.ChallengesCompetitions.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChallengesCompetitions.Config.RabbitMQConfig;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Challenge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;
import tn.esprit.language_courses_service.DTO.ChallengeDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataSyncService {

    private final ChallengeRepository challengeRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Synchronizes all existing challenges to Elasticsearch via RabbitMQ.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void syncAllChallenges() {
        System.out.println("Scheduled synchronization: Sending all Challenges to RabbitMQ...");
        List<Challenge> challenges = challengeRepository.findAll();

        for (Challenge challenge : challenges) {
            sendChallengeToQueue(challenge);
        }

        log.info("Successfully sent {} challenges to {}", challenges.size(), RabbitMQConfig.CHALLENGE_QUEUE);
    }

    public void sendChallengeToQueue(Challenge challenge) {
        ChallengeDTO challengeDTO = ChallengeDTO.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .type(challenge.getType() != null ? challenge.getType().name() : null)
                .level(challenge.getLevel() != null ? challenge.getLevel().name() : null)
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.CHALLENGE_QUEUE, challengeDTO);
        log.debug("Sent Challenge {} to queue", challenge.getId());
        System.out.println("📤 [Activity Service] Challenge [" + challenge.getTitle() + "] sent to queue for indexing.");
    }
}
