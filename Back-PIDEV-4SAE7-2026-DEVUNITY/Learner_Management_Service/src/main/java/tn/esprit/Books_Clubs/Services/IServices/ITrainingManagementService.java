package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.jungleinenglishuser.entities.Payment;
import tn.esprit.jungleinenglishuser.entities.TrainingParticipation;

public interface ITrainingManagementService {

    Payment payTrainingCash(Integer memberId, Long trainingId);

    Payment confirmCashPayment(Long paymentId);

    Payment payTrainingWithWallet(Integer memberId, Long trainingId);

    TrainingParticipation completeTraining(Long participationId, Integer score);
}