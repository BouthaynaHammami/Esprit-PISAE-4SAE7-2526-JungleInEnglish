package tn.esprit.language_courses_service.ChallengesCompetitions.Producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.DTO.ChallengeDTO;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Challenge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Repositories.ChallengeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ChallengeRepository challengeRepository;

    public void sendChallenge(ChallengeDTO challengeDTO) {
        rabbitTemplate.convertAndSend("challenge.queue", challengeDTO);
        System.out.println("Challenge message sent: " + challengeDTO.getTitle());
    }

    public void syncAllChallenges() {
        List<Challenge> challenges = challengeRepository.findAll();
        for (Challenge challenge : challenges) {
            ChallengeDTO dto = ChallengeDTO.builder()
                    .id(challenge.getId())
                    .title(challenge.getTitle())
                    .description(challenge.getDescription())
                    .type(challenge.getType().toString())
                    .level(challenge.getLevel() != null ? challenge.getLevel().toString() : null)
                    .startDate(challenge.getStartDate())
                    .endDate(challenge.getEndDate())
                    .build();
            sendChallenge(dto);
        }
        System.out.println("All existing challenges synchronized via RabbitMQ.");
    }
}
