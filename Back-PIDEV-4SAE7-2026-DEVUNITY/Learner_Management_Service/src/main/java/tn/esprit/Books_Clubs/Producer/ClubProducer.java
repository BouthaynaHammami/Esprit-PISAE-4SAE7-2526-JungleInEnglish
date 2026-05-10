package tn.esprit.Books_Clubs.Producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.Config.RabbitMQConfig;
import tn.esprit.Books_Clubs.DTO.ClubDTO;
import tn.esprit.Books_Clubs.entities.Club;
import tn.esprit.Books_Clubs.repositories.ClubRepository;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ClubRepository clubRepository;

    public void sendClub(ClubDTO clubDTO) {
        log.info("Sending Club to RabbitMQ: {}", clubDTO.getName());
        rabbitTemplate.convertAndSend(RabbitMQConfig.CLUB_QUEUE, clubDTO);
    }

    public void syncAllClubs() {
        log.info("Synchronizing all existing clubs to Elasticsearch...");
        List<Club> clubs = clubRepository.findAll();
        for (Club club : clubs) {
            ClubDTO dto = ClubDTO.builder()
                    .clubId(club.getClubId())
                    .name(club.getName())
                    .description(club.getDescription())
                    .type(club.getType() != null ? club.getType().name() : null)
                    .status(club.getStatus() != null ? club.getStatus().name() : null)
                    .creationDate(club.getCreationDate() != null ? Date.from(club.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null)
                    .build();
            sendClub(dto);
        }
        log.info("Synchronization of {} clubs completed.", clubs.size());
    }
}
