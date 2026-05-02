package tn.esprit.Books_Clubs.Services.ImplServices;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.Services.IServices.IRequestService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.Books_Clubs.repositories.ClubRepository;
import tn.esprit.Books_Clubs.repositories.MembershipRequestRepository;
import tn.esprit.Books_Clubs.repositories.ParticipationClubRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements IRequestService {

    private final ClubRepository clubRepository;
    private final MembershipRequestRepository requestRepository;
    private final ParticipationClubRepository participationRepository;

    @Override
    public MembershipRequest create(Long memberId, Long clubId, String motivation) {
        if (requestRepository.existsByMemberIdAndClub_ClubIdAndStatus(memberId, clubId, RequestStatus.PENDING))
            throw new IllegalStateException("Request already pending");

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found: " + clubId));

        MembershipRequest req = new MembershipRequest();
        req.setMemberId(memberId);
        req.setClub(club);
        req.setMotivation(motivation);
        req.setStatus(RequestStatus.PENDING);
        req.setRequestDate(LocalDate.now());

        return requestRepository.save(req);
    }

    @Override
    public MembershipRequest accept(Long requestId, Long decidedByMemberId) {
        MembershipRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));

        req.setStatus(RequestStatus.ACCEPTED);
        req.setDecisionDate(LocalDate.now());
        req.setDecidedByMemberId(decidedByMemberId);
        requestRepository.save(req);

        // create participation if not exists
        if (!participationRepository.existsByMemberIdAndClub_ClubId(req.getMemberId(), req.getClub().getClubId())) {
            ParticipationClub pc = new ParticipationClub();
            pc.setMemberId(req.getMemberId());
            pc.setClub(req.getClub());
            pc.setJoinDate(LocalDate.now());
            pc.setRole(MembershipRole.MEMBER);
            pc.setStatus(MembershipStatus.ACTIVE);
            participationRepository.save(pc);
        }

        return req;
    }

    @Override
    public MembershipRequest reject(Long requestId, Long decidedByMemberId) {
        MembershipRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));

        req.setStatus(RequestStatus.REJECTED);
        req.setDecisionDate(LocalDate.now());
        req.setDecidedByMemberId(decidedByMemberId);

        return requestRepository.save(req);
    }

    @Override public List<MembershipRequest> byClub(Long clubId) { return requestRepository.findByClub_ClubId(clubId); }
    @Override public List<MembershipRequest> byMember(Long memberId) { return requestRepository.findByMemberId(memberId); }
}
