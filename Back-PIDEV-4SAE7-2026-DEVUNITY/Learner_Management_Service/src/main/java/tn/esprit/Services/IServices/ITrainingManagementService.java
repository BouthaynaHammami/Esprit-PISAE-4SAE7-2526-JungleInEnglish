package tn.esprit.Services.IServices;

import tn.esprit.Books_Clubs.entities.Payment;
import tn.esprit.Books_Clubs.entities.TrainingParticipation;

public interface ITrainingManagementService {

    Payment payTrainingCash(Integer memberId, Long trainingId);

    Payment confirmCashPayment(Long paymentId);

    Payment payTrainingWithWallet(Integer memberId, Long trainingId);

    TrainingParticipation completeTraining(Long participationId, Integer score);
}