package tn.esprit.language_courses_service.ChildrenEnglish.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Reward;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.TypeReward;
import tn.esprit.language_courses_service.ChildrenEnglish.Serivces.RewardService;

import java.util.List;

@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RewardController {
    
    private final RewardService rewardService;
    
    @GetMapping
    public ResponseEntity<List<Reward>> getAllRewards() {
        return ResponseEntity.ok(rewardService.getAllRewards());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Reward> getRewardById(@PathVariable Long id) {
        return ResponseEntity.ok(rewardService.getReward(id));
    }
    
    @PostMapping("/add")
    public ResponseEntity<Reward> addReward(@RequestBody Reward reward) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rewardService.addReward(reward));
    }
    
    @PutMapping("/update")
    public ResponseEntity<Reward> updateReward(@RequestBody Reward reward) {
        return ResponseEntity.ok(rewardService.updateReward(reward));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Reward>> getRewardsByType(@PathVariable TypeReward type) {
        return ResponseEntity.ok(rewardService.getRewardsByType(type));
    }
    
    @GetMapping("/available/{points}")
    public ResponseEntity<List<Reward>> getAvailableRewards(@PathVariable Integer points) {
        return ResponseEntity.ok(rewardService.getAvailableRewards(points));
    }
}
