package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.*;

import java.util.List;

public interface ParticipationClubRepository extends JpaRepository<ParticipationClub, Long> {
    List<ParticipationClub> findByClub_ClubId(Long clubId);
    List<ParticipationClub> findByMemberId(Long memberId);
    boolean existsByMemberIdAndClub_ClubId(Long memberId, Long clubId);
}
