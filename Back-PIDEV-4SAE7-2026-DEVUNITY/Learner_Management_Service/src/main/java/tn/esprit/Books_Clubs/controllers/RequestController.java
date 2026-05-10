package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.jungleinenglishuser.Services.IServices.IRequestService;
import tn.esprit.jungleinenglishuser.entities.MembershipRequest;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final IRequestService requestService;

    @PostMapping
    public MembershipRequest create(@RequestParam Long memberId,
                                    @RequestParam Long clubId,
                                    @RequestParam String motivation) {
        try {
            return requestService.create(memberId, clubId, motivation);
        } catch (IllegalStateException e) {
            // ✅ Retourne 409 Conflict au lieu de 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
    public MembershipRequest accept(@PathVariable Long id, @RequestParam Long decidedByMemberId) {
        return requestService.accept(id, decidedByMemberId);
    }

    @PutMapping("/{id}/reject")
    public MembershipRequest reject(@PathVariable Long id, @RequestParam Long decidedByMemberId) {
        return requestService.reject(id, decidedByMemberId);
    }

    @GetMapping("/club/{clubId}")
    public List<MembershipRequest> byClub(@PathVariable Long clubId) {
        return requestService.byClub(clubId);
    }

    @GetMapping("/member/{memberId}")
    public List<MembershipRequest> byMember(@PathVariable Long memberId) {
        return requestService.byMember(memberId);
    }
}