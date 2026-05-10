package tn.esprit.Books_Clubs.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.jungleinenglishuser.Services.IServices.IWalletService;
import tn.esprit.jungleinenglishuser.entities.Transaction;
import tn.esprit.jungleinenglishuser.entities.Wallet;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/wallet")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class WalletController {

    private final IWalletService walletService;

    // 🟢 CREATE WALLET
    @PostMapping("/create/{userId}")
    public Wallet createWallet(@PathVariable Integer userId) {
        return walletService.createWallet(userId);
    }

    // 💰 RECHARGE
    @PostMapping("/recharge")
    public Wallet recharge(@RequestParam Integer userId,
                           @RequestParam BigDecimal amount) {
        return walletService.recharge(userId, amount);
    }

    // 💳 PAY FROM WALLET
    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam Integer userId,
                                 @RequestParam BigDecimal amount) {
        try {
            walletService.payFromWallet(userId, amount);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 💳 GET WALLET
    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(walletService.getWallet(userId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 📜 TRANSACTIONS
    @GetMapping("/transactions/{userId}")
    public List<Transaction> getTransactions(@PathVariable Integer userId) {
        return walletService.getTransactions(userId);
    }
}