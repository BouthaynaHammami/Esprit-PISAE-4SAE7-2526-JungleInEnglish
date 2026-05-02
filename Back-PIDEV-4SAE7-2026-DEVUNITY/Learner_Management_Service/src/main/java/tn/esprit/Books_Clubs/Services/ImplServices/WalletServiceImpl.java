package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.Books_Clubs.Services.IServices.IWalletService;
import tn.esprit.Books_Clubs.entities.Transaction;
import tn.esprit.Books_Clubs.entities.Wallet;
import tn.esprit.Books_Clubs.repositories.TransactionRepository;
import tn.esprit.Books_Clubs.repositories.WalletRepository;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {

    private final WalletRepository walletRepo;
    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    // ðŸ”¹ rÃ©cupÃ©rer user
    private User getUser(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public Wallet createWallet(Integer userId) {

        User user = getUser(userId);

        return walletRepo.findByUser(user).orElseGet(() -> {
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.ZERO);
            return walletRepo.save(wallet);
        });
    }

    @Override
    @Transactional
    public Wallet recharge(Integer userId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("Amount must be > 0");

        User user = getUser(userId);

        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));

        Transaction t = new Transaction();
        t.setAmount(amount);
        t.setType("CREDIT");
        t.setDate(LocalDateTime.now());
        t.setUser(user);      // âœ… relation
        t.setWallet(wallet);  // âœ… relation

        transactionRepo.save(t);

        return walletRepo.save(wallet);
    }

    @Override
    @Transactional
    public void payFromWallet(Integer userId, BigDecimal amount) {

        User user = getUser(userId);

        Wallet wallet = walletRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Solde insuffisant");

        wallet.setBalance(wallet.getBalance().subtract(amount));

        Transaction t = new Transaction();
        t.setAmount(amount);
        t.setType("DEBIT");
        t.setDate(LocalDateTime.now());
        t.setUser(user);      // âœ…
        t.setWallet(wallet);  // âœ…

        transactionRepo.save(t);

        walletRepo.save(wallet);
    }

    @Override
    public Wallet getWallet(Integer userId) {

        User user = getUser(userId);

        return walletRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public List<Transaction> getTransactions(Integer userId) {

        User user = getUser(userId);

        return transactionRepo.findByUser(user);
    }
}
