package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.jungleinenglishuser.Services.IServices.ITrainingManagementService;
import tn.esprit.jungleinenglishuser.entities.*;
import tn.esprit.jungleinenglishuser.repositories.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingManagementServiceImpl implements ITrainingManagementService {

    private final TrainingRepository trainingRepository;
    private final TrainingParticipationRepository participationRepository;
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private Training findTraining(Long trainingId) {
        return trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found: " + trainingId));
    }

    private TrainingParticipation findParticipation(Long participationId) {
        return participationRepository.findById(participationId)
                .orElseThrow(() -> new IllegalArgumentException("Participation not found: " + participationId));
    }

    private User findUser(Integer memberId) {
        return userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found: " + memberId));
    }

    private Wallet findWallet(Integer memberId) {
        User user = findUser(memberId);
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found for member: " + memberId));
    }

    private TrainingParticipation findParticipationByMemberAndTraining(Integer memberId, Long trainingId) {
        return participationRepository.findByMember_UserIdAndTraining_TrainingId(memberId, trainingId)
                .orElseThrow(() -> new IllegalStateException("Vous devez être inscrit au training"));
    }

    private boolean isTrainingFinished(Training training) {
        if (training.getEndDate() == null) {
            throw new IllegalStateException("Training end date is required");
        }

        return training.getStatus() == ActivityStatus.FINISHED
                || LocalDate.now().isAfter(training.getEndDate())
                || LocalDate.now().isEqual(training.getEndDate());
    }

    private BigDecimal calculateReward(BigDecimal rewardMax, int score) {
        if (rewardMax == null || rewardMax.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return rewardMax
                .multiply(BigDecimal.valueOf(score))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private void checkTrainingPaymentAlreadyExists(Integer memberId, Long trainingId) {
        paymentRepository.findByMember_UserIdAndTraining_TrainingId(memberId, trainingId)
                .ifPresent(p -> {
                    if (p.getStatus() == PaymentStatus.PENDING) {
                        throw new IllegalStateException("Paiement déjà en attente de validation");
                    }
                    if (p.getStatus() == PaymentStatus.PAID) {
                        throw new IllegalStateException("Formation déjà payée");
                    }
                });
    }

    @Override
    public Payment payTrainingCash(Integer memberId, Long trainingId) {
        Training training = findTraining(trainingId);
        User member = findUser(memberId);

        findParticipationByMemberAndTraining(memberId, trainingId);

        if (training.getPrice() == null || training.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Invalid training price");
        }

        checkTrainingPaymentAlreadyExists(memberId, trainingId);

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setTraining(training);
        payment.setAmount(training.getPrice());
        payment.setMethod(PaymentMethod.CASH);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Override
    public Payment confirmCashPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + paymentId));

        if (payment.getMethod() != PaymentMethod.CASH) {
            throw new IllegalStateException("This payment is not CASH");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending cash payment can be confirmed");
        }

        payment.setStatus(PaymentStatus.PAID);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment payTrainingWithWallet(Integer memberId, Long trainingId) {
        Training training = findTraining(trainingId);
        Wallet wallet = findWallet(memberId);
        User user = findUser(memberId);

        findParticipationByMemberAndTraining(memberId, trainingId);

        if (training.getPrice() == null || training.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Invalid training price");
        }

        checkTrainingPaymentAlreadyExists(memberId, trainingId);

        if (wallet.getBalance() == null) {
            wallet.setBalance(BigDecimal.ZERO);
        }

        if (wallet.getBalance().compareTo(training.getPrice()) < 0) {
            throw new RuntimeException("Solde insuffisant");
        }

        wallet.setBalance(wallet.getBalance().subtract(training.getPrice()));
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setAmount(training.getPrice());
        transaction.setType("DEBIT");
        transaction.setDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);

        Payment payment = new Payment();
        payment.setMember(user);
        payment.setTraining(training);
        payment.setAmount(training.getPrice());
        payment.setMethod(PaymentMethod.WALLET);
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    @Override
    public TrainingParticipation completeTraining(Long participationId, Integer score) {
        TrainingParticipation participation = findParticipation(participationId);
        Training training = participation.getTraining();

        if (score == null || score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        participation.setScore(score);
        participation.setCompleted(true);

        if (score < 50) {
            participation.setStatus(ParticipationStatus.FAILED);
        } else {
            participation.setStatus(ParticipationStatus.CONFIRMED);
        }

        if (Boolean.TRUE.equals(training.getRewardEnabled())
                && !Boolean.TRUE.equals(participation.getRewardTransferred())
                && score >= 50) {

            Integer memberId = participation.getMember().getUserId();
            Wallet wallet = findWallet(memberId);
            User user = findUser(memberId);

            BigDecimal reward = calculateReward(training.getRewardMax(), score);

            if (wallet.getBalance() == null) {
                wallet.setBalance(BigDecimal.ZERO);
            }

            wallet.setBalance(wallet.getBalance().add(reward));
            walletRepository.save(wallet);

            Transaction transaction = new Transaction();
            transaction.setAmount(reward);
            transaction.setType("CREDIT");
            transaction.setDate(LocalDateTime.now());
            transaction.setUser(user);
            transaction.setWallet(wallet);
            transactionRepository.save(transaction);

            participation.setRewardAmount(reward);
            participation.setRewardTransferred(true);
        }

        return participationRepository.save(participation);
    }
}