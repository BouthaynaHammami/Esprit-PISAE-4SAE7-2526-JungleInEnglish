package tn.esprit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.MembershipRequest;
import tn.esprit.Books_Clubs.entities.RequestStatus;

import java.util.List;

public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {

    List<MembershipRequest> findByClub_ClubId(Long clubId);

    List<MembershipRequest> findByMember_UserId(Integer userId);

    boolean existsByMember_UserIdAndClub_ClubIdAndStatus(Integer userId, Long clubId, RequestStatus status);
}