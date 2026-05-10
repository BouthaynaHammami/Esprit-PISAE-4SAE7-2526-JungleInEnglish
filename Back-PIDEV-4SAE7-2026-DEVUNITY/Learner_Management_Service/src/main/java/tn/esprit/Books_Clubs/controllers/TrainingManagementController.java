package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.jungleinenglishuser.Services.IServices.ITrainingManagementService;
import tn.esprit.jungleinenglishuser.entities.Payment;
import tn.esprit.jungleinenglishuser.entities.TrainingParticipation;

import java.util.Map;

@RestController
@RequestMapping("/api/training-management")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TrainingManagementController {

    private final ITrainingManagementService trainingManagementService;

    // ===================== PAYMENT =====================

    @PostMapping("/pay/cash")
    public ResponseEntity<Payment> payCash(
            @RequestParam Integer memberId,
            @RequestParam Long trainingId
    ) {
        return ResponseEntity.ok(
                trainingManagementService.payTrainingCash(memberId, trainingId)
        );
    }

    @PutMapping("/pay/cash/{paymentId}/confirm")
    public ResponseEntity<Payment> confirmCash(@PathVariable Long paymentId) {
        return ResponseEntity.ok(
                trainingManagementService.confirmCashPayment(paymentId)
        );
    }

    @PostMapping("/pay/wallet")
    public ResponseEntity<Payment> payWallet(
            @RequestParam Integer memberId,
            @RequestParam Long trainingId
    ) {
        return ResponseEntity.ok(
                trainingManagementService.payTrainingWithWallet(memberId, trainingId)
        );
    }

    // ===================== COMPLETE TRAINING =====================

    @PutMapping("/complete/{participationId}")
    public ResponseEntity<TrainingParticipation> completeTraining(
            @PathVariable Long participationId,
            @RequestParam Integer score
    ) {
        return ResponseEntity.ok(
                trainingManagementService.completeTraining(participationId, score)
        );
    }

    // ===================== TEST =====================

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> status() {
        return ResponseEntity.ok(Map.of("message", "Training management OK"));
    }
}