package tn.esprit.Books_Clubs.Services.ImplServices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.Books_Clubs.entities.Transaction;
import tn.esprit.Books_Clubs.entities.Wallet;
import tn.esprit.repositories.TransactionRepository;
import tn.esprit.repositories.WalletRepository;
import tn.esprit.learner_managment_service.UserManagement.Entities.Role;
import tn.esprit.learner_managment_service.UserManagement.Entities.User;
import tn.esprit.learner_managment_service.UserManagement.Repositories.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepo;
    @Mock
    private TransactionRepository transactionRepo;
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private WalletServiceImpl walletService;

    @Test
    void recharge_shouldIncreaseBalanceAndCreateCreditTransaction() {
        User user = User.builder().userId(1).email("u@x.com").role(Role.STUDENT).build();
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(new BigDecimal("10.00"));

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));
        when(walletRepo.save(wallet)).thenReturn(wallet);

        Wallet result = walletService.recharge(1, new BigDecimal("5.50"));

        assertEquals(new BigDecimal("15.50"), result.getBalance());
        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepo).save(txCaptor.capture());
        assertEquals("CREDIT", txCaptor.getValue().getType());
        assertEquals(new BigDecimal("5.50"), txCaptor.getValue().getAmount());
    }

    @Test
    void payFromWallet_shouldThrowWhenBalanceInsufficient() {
        User user = User.builder().userId(1).email("u@x.com").role(Role.STUDENT).build();
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(new BigDecimal("3.00"));

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(walletRepo.findByUser(user)).thenReturn(Optional.of(wallet));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> walletService.payFromWallet(1, new BigDecimal("9.00")));

        assertEquals("Solde insuffisant", ex.getMessage());
        verify(transactionRepo, never()).save(any(Transaction.class));
    }
}
