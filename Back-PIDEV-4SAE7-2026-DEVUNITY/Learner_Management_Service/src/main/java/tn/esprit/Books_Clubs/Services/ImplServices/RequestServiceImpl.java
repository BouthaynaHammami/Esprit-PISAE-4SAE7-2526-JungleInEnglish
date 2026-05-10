package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.jungleinenglishuser.Services.IServices.IRequestService;
import tn.esprit.jungleinenglishuser.entities.*;
import tn.esprit.jungleinenglishuser.repositories.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements IRequestService {

    private final ClubRepository clubRepository;
    private final MembershipRequestRepository requestRepository;
    private final ParticipationClubRepository participationRepository;
    private final UserRepository userRepository;

    @Override
    public MembershipRequest create(Long memberId, Long clubId, String motivation) {

        if (requestRepository.existsByMember_UserIdAndClub_ClubIdAndStatus(
                memberId.intValue(), clubId, RequestStatus.PENDING))
            throw new IllegalStateException("Request already pending");

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found: " + clubId));

        User member = userRepository.findById(memberId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MembershipRequest req = new MembershipRequest();
        req.setMember(member);
        req.setClub(club);
        req.setMotivation(motivation);
        req.setStatus(RequestStatus.PENDING);
        req.setRequestDate(LocalDate.now());

        return requestRepository.save(req);
    }

    @Override
    public MembershipRequest accept(Long requestId, Long decidedByMemberId) {

        MembershipRequest req = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        User decidedBy = userRepository.findById(decidedByMemberId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        req.setStatus(RequestStatus.ACCEPTED);
        req.setDecisionDate(LocalDate.now());
        req.setDecidedByMember(decidedBy);

        requestRepository.save(req);

        // create participation
        if (!participationRepository.existsByMember_UserIdAndClub_ClubId(
                req.getMember().getUserId(), req.getClub().getClubId())) {

            ParticipationClub pc = new ParticipationClub();
            pc.setMember(req.getMember());
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
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        User decidedBy = userRepository.findById(decidedByMemberId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        req.setStatus(RequestStatus.REJECTED);
        req.setDecisionDate(LocalDate.now());
        req.setDecidedByMember(decidedBy);

        return requestRepository.save(req);
    }

    @Override
    public List<MembershipRequest> byClub(Long clubId) {
        return requestRepository.findByClub_ClubId(clubId);
    }

    @Override
    public List<MembershipRequest> byMember(Long memberId) {
        return requestRepository.findByMember_UserId(memberId.intValue());
    }
}