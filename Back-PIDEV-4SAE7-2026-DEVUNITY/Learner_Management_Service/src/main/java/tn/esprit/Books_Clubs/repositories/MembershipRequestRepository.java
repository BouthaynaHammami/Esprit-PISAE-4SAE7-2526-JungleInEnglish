package tn.esprit.Books_Clubs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.Books_Clubs.entities.MembershipRequest;
import tn.esprit.Books_Clubs.entities.RequestStatus;

import java.util.List;

public interface MembershipRequestRepository extends JpaRepository<MembershipRequest, Long> {
    List<MembershipRequest> findByClub_ClubId(Long clubId);
    List<MembershipRequest> findByMemberId(Long memberId);
    boolean existsByMemberIdAndClub_ClubIdAndStatus(Long memberId, Long clubId, RequestStatus status);
}
