package tn.esprit.Books_Clubs.Services.ImplServices;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.Services.IServices.IClubService;
import tn.esprit.Books_Clubs.entities.Club;
import tn.esprit.Books_Clubs.repositories.ClubRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements IClubService {

    private final ClubRepository clubRepository;

    @Override public Club add(Club club) { return clubRepository.save(club); }
    @Override public Club update(Club club) { return clubRepository.save(club); }
    @Override public void delete(Long id) { clubRepository.deleteById(id); }

    @Override
    public Club findById(Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Club not found: " + id));
    }

    @Override public List<Club> findAll() { return clubRepository.findAll(); }
}
