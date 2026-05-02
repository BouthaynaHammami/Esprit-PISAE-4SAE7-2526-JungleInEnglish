package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.Books_Clubs.entities.ParticipationClub;

import java.util.List;

public interface IMembershipService {
    ParticipationClub joinClub(Long memberId, Long clubId);
    List<ParticipationClub> membersOfClub(Long clubId);
    List<ParticipationClub> clubsOfMember(Long memberId);
}
