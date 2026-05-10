package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.Services.IServices.IClubService;
import tn.esprit.Books_Clubs.entities.Club;
import tn.esprit.Books_Clubs.repositories.ClubRepository;
import tn.esprit.Books_Clubs.Producer.ClubProducer;
import tn.esprit.Books_Clubs.DTO.ClubDTO;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements IClubService {

    private final ClubRepository clubRepository;
    private final ClubProducer clubProducer;

    @Override 
    public Club add(Club club) { 
        Club saved = clubRepository.save(club); 
        sendToElastic(saved);
        return saved; 
    }

    @Override 
    public Club update(Club club) { 
        Club updated = clubRepository.save(club); 
        sendToElastic(updated);
        return updated; 
    }

    @Override public void delete(Long id) { clubRepository.deleteById(id); }

    @Override
    public Club findById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Club not found: " + id));
    }

    @Override public List<Club> findAll() { return clubRepository.findAll(); }

    private void sendToElastic(Club club) {
        clubProducer.sendClub(ClubDTO.builder()
                .clubId(club.getClubId())
                .name(club.getName())
                .description(club.getDescription())
                .type(club.getType() != null ? club.getType().name() : null)
                .status(club.getStatus() != null ? club.getStatus().name() : null)
                .creationDate(club.getCreationDate() != null ? Date.from(club.getCreationDate().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null)
                .build());
    }
}
