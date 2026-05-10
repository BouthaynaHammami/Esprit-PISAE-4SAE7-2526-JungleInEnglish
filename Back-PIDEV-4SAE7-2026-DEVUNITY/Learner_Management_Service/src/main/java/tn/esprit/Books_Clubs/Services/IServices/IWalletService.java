package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.jungleinenglishuser.entities.Transaction;
import tn.esprit.jungleinenglishuser.entities.Wallet;

import java.math.BigDecimal;
import java.util.List;

public interface IWalletService {

    Wallet createWallet(Integer userId);

    Wallet recharge(Integer userId, BigDecimal amount);

    void payFromWallet(Integer userId, BigDecimal amount);

    Wallet getWallet(Integer userId);

    List<Transaction> getTransactions(Integer userId);
}