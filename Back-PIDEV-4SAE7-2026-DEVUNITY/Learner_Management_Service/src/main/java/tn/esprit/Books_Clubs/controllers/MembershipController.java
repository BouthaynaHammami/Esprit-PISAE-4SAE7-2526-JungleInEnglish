package tn.esprit.Books_Clubs.Controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.Services.IServices.IMembershipService;
import tn.esprit.Books_Clubs.entities.ParticipationClub;

import java.util.List;

@RestController
@RequestMapping("/api/memberships")
@RequiredArgsConstructor
public class MembershipController {

    private final IMembershipService membershipService;

    @PostMapping("/join")
    public ParticipationClub join(@RequestParam Long memberId, @RequestParam Long clubId) {
        return membershipService.joinClub(memberId, clubId);
    }

    @GetMapping("/club/{clubId}")
    public List<ParticipationClub> membersOfClub(@PathVariable Long clubId) {
        return membershipService.membersOfClub(clubId);
    }

    @GetMapping("/member/{memberId}")
    public List<ParticipationClub> clubsOfMember(@PathVariable Long memberId) {
        return membershipService.clubsOfMember(memberId);
    }
}