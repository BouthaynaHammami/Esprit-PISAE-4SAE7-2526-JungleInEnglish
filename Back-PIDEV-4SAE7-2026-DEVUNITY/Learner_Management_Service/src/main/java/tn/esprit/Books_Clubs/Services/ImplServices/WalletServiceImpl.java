package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.jungleinenglishuser.Services.IServices.IWalletService;
import tn.esprit.jungleinenglishuser.entities.Transaction;
import tn.esprit.jungleinenglishuser.entities.User;
import tn.esprit.jungleinenglishuser.entities.Wallet;
import tn.esprit.jungleinenglishuser.repositories.TransactionRepository;
import tn.esprit.jungleinenglishuser.repositories.UserRepository;
import tn.esprit.jungleinenglishuser.repositories.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {

    private final WalletRepository walletRepo;
    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    private User getUser(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Wallet getOrCreateWallet(User user) {
        return walletRepo.findByUser(user).orElseGet(() -> {
            Wallet wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.ZERO);
            return walletRepo.save(wallet);
        });
    }

    @Override
    @Transactional
    public Wallet createWallet(Integer userId) {
        User user = getUser(userId);
        return getOrCreateWallet(user);
    }

    @Override
    @Transactional
    public Wallet recharge(Integer userId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be > 0");
        }

        User user = getUser(userId);
        Wallet wallet = getOrCreateWallet(user);

        wallet.setBalance(wallet.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType("CREDIT");
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setWallet(wallet);

        transactionRepo.save(transaction);

        return walletRepo.save(wallet);
    }

    @Override
    @Transactional
    public void payFromWallet(Integer userId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be > 0");
        }

        User user = getUser(userId);
        Wallet wallet = getOrCreateWallet(user);

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType("DEBIT");
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setWallet(wallet);

        transactionRepo.save(transaction);
        walletRepo.save(wallet);
    }

    @Override
    @Transactional
    public Wallet getWallet(Integer userId) {
        User user = getUser(userId);
        return getOrCreateWallet(user);
    }

    @Override
    public List<Transaction> getTransactions(Integer userId) {
        User user = getUser(userId);
        return transactionRepo.findByUser(user);
    }
}