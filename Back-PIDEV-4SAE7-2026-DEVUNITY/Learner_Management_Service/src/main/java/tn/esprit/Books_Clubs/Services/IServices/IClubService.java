package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.Books_Clubs.entities.Club;

import java.util.List;

public interface IClubService {
    Club add(Club club);
    Club update(Club club);
    void delete(Long id);
    Club findById(Long id);
    List<Club> findAll();
}
