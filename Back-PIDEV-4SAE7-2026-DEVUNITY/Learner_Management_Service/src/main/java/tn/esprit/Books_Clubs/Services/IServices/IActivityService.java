package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.jungleinenglishuser.entities.*;

import java.util.List;

public interface IActivityService {

    // CRUD activities
    Excursion addExcursion(Excursion e);
    Training addTraining(Training t);

    Excursion updateExcursion(Excursion e);
    Training updateTraining(Training t);

    void deleteExcursion(Long id);
    void deleteTraining(Long id);

    Excursion findExcursion(Long id);
    Training findTraining(Long id);

    List<Excursion> excursionsByClub(Long clubId);
    List<Training> trainingsByClub(Long clubId);

    // registrations
    ExcursionParticipation registerExcursion(Long memberId, Long excursionId);
    TrainingParticipation registerTraining(Long memberId, Long trainingId);

    List<ExcursionParticipation> excursionParticipants(Long excursionId);
    List<TrainingParticipation> trainingParticipants(Long trainingId);
}