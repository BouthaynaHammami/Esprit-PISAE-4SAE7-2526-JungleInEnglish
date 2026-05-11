package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.ParticipationClub;

import java.util.List;

public interface ParticipationClubRepository extends JpaRepository<ParticipationClub, Long> {

    List<ParticipationClub> findByClub_ClubId(Long clubId);

    List<ParticipationClub> findByMember_UserId(Integer userId);

    boolean existsByMember_UserIdAndClub_ClubId(Integer userId, Long clubId);
}