package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.jungleinenglishuser.Services.IServices.IClubService;
import tn.esprit.jungleinenglishuser.entities.Club;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final IClubService clubService;

    @PostMapping public Club add(@RequestBody Club c) { return clubService.add(c); }
    @PutMapping public Club update(@RequestBody Club c) { return clubService.update(c); }
    @GetMapping("/{id}") public Club find(@PathVariable Long id) { return clubService.findById(id); }
    @GetMapping public List<Club> all() { return clubService.findAll(); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { clubService.delete(id); }
}