package tn.esprit.Books_Clubs.Services.ImplServices;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.Services.IServices.IMembershipService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.Books_Clubs.repositories.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements IMembershipService {

    private final ClubRepository clubRepository;
    private final ParticipationClubRepository participationRepository;

    @Override
    public ParticipationClub joinClub(Long memberId, Long clubId) {
        if (participationRepository.existsByMemberIdAndClub_ClubId(memberId, clubId))
            throw new IllegalStateException("Member already joined this club");

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found: " + clubId));

        ParticipationClub pc = new ParticipationClub();
        pc.setMemberId(memberId);
        pc.setClub(club);
        pc.setJoinDate(LocalDate.now());
        pc.setRole(MembershipRole.MEMBER);
        pc.setStatus(MembershipStatus.ACTIVE);

        return participationRepository.save(pc);
    }

    @Override public List<ParticipationClub> membersOfClub(Long clubId) { return participationRepository.findByClub_ClubId(clubId); }
    @Override public List<ParticipationClub> clubsOfMember(Long memberId) { return participationRepository.findByMemberId(memberId); }
}

