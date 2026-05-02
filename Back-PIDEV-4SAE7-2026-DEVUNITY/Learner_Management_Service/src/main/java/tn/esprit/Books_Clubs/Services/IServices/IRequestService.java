package tn.esprit.Books_Clubs.Services.IServices;


import tn.esprit.Books_Clubs.entities.MembershipRequest;

import java.util.List;

public interface IRequestService {
    MembershipRequest create(Long memberId, Long clubId, String motivation);
    MembershipRequest accept(Long requestId, Long decidedByMemberId);
    MembershipRequest reject(Long requestId, Long decidedByMemberId);
    List<MembershipRequest> byClub(Long clubId);
    List<MembershipRequest> byMember(Long memberId);
}

