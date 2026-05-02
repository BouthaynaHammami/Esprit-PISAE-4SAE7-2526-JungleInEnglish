package tn.esprit.Books_Clubs.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.Books_Clubs.Services.IServices.IActivityService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.Books_Clubs.repositories.*;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements IActivityService {

    private final ClubRepository clubRepository;
    private final ExcursionRepository excursionRepository;
    private final TrainingRepository trainingRepository;
    private final ExcursionParticipationRepository exPartRepository;
    private final TrainingParticipationRepository trPartRepository;

    // ============================== CRUD ==============================
    @Override
    public Excursion addExcursion(Excursion e) {
        clubRepository.findById(e.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        if (e.getStatus() == null) e.setStatus(ActivityStatus.PLANNED);
        if (e.getNbrDeReservation() == null) e.setNbrDeReservation(0);

        return excursionRepository.save(e);
    }

    @Override
    public Training addTraining(Training t) {
        clubRepository.findById(t.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        if (t.getStatus() == null) t.setStatus(ActivityStatus.PLANNED);
        if (t.getNbrDeReservation() == null) t.setNbrDeReservation(0);

        return trainingRepository.save(t);
    }

    @Override public Excursion updateExcursion(Excursion e) { return excursionRepository.save(e); }
    @Override public Training updateTraining(Training t) { return trainingRepository.save(t); }

    @Override public void deleteExcursion(Long id) { excursionRepository.deleteById(id); }
    @Override public void deleteTraining(Long id) { trainingRepository.deleteById(id); }

    @Override
    public Excursion findExcursion(Long id) {
        return excursionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Excursion not found: " + id));
    }

    @Override
    public Training findTraining(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Training not found: " + id));
    }

    @Override public List<Excursion> excursionsByClub(Long clubId) { return excursionRepository.findByClub_ClubId(clubId); }
    @Override public List<Training> trainingsByClub(Long clubId) { return trainingRepository.findByClub_ClubId(clubId); }

    // ============================== HELPERS ==============================
    private void validateCapacity(Integer capacity) {
        if (capacity == null || capacity <= 0) {
            throw new IllegalStateException("Invalid capacity (nbrDeplace)");
        }
    }

    private void ensureActivityOpen(ActivityStatus status, String type) {
        if (status == ActivityStatus.CANCELLED || status == ActivityStatus.FINISHED) {
            throw new IllegalStateException(type + " not available (status=" + status + ")");
        }
    }

    // ============================== REGISTER ==============================
    @Override
    public ExcursionParticipation registerExcursion(Long memberId, Long excursionId) {

        if (exPartRepository.existsByMemberIdAndExcursion_ExcursionId(memberId, excursionId)) {
            throw new IllegalStateException("Already registered");
        }

        Excursion ex = findExcursion(excursionId);

        ensureActivityOpen(ex.getStatus(), "Excursion");
        validateCapacity(ex.getNbrDeplace());

        int reserved = ex.getNbrDeReservation() == null ? 0 : ex.getNbrDeReservation();
        int capacity = ex.getNbrDeplace();

        if (reserved >= capacity) {
            // activitÃ© pleine -> on bloque
            // (tu peux changer status ici si tu veux, mais recommandÃ©: laisser PLANNED)
            throw new IllegalStateException("Excursion is full");
        }

        // save participation
        ExcursionParticipation p = new ExcursionParticipation();
        p.setMemberId(memberId);
        p.setExcursion(ex);
        p.setRegistrationDate(LocalDate.now());
        p.setStatus(ParticipationStatus.REGISTERED);

        ExcursionParticipation saved = exPartRepository.save(p);

        // increment reservations counter
        ex.setNbrDeReservation(reserved + 1);

        // si plein aprÃ¨s ajout
        if (ex.getNbrDeReservation() >= capacity) {
            // OPTION 1 (recommandÃ©e): laisser PLANNED et juste bloquer les nouvelles inscriptions
            ex.setStatus(ActivityStatus.PLANNED);

            // OPTION 2 (si tu INSISTES): ex.setStatus(ActivityStatus.CANCELLED);
        }

        excursionRepository.save(ex);
        return saved;
    }

    @Override
    public TrainingParticipation registerTraining(Long memberId, Long trainingId) {

        if (trPartRepository.existsByMemberIdAndTraining_TrainingId(memberId, trainingId)) {
            throw new IllegalStateException("Already registered");
        }

        Training tr = findTraining(trainingId);

        ensureActivityOpen(tr.getStatus(), "Training");
        validateCapacity(tr.getNbrDeplace());

        int reserved = tr.getNbrDeReservation() == null ? 0 : tr.getNbrDeReservation();
        int capacity = tr.getNbrDeplace();

        if (reserved >= capacity) {
            throw new IllegalStateException("Training is full");
        }

        TrainingParticipation p = new TrainingParticipation();
        p.setMemberId(memberId);
        p.setTraining(tr);
        p.setRegistrationDate(LocalDate.now());
        p.setStatus(ParticipationStatus.REGISTERED);

        TrainingParticipation saved = trPartRepository.save(p);

        tr.setNbrDeReservation(reserved + 1);

        if (tr.getNbrDeReservation() >= capacity) {
            tr.setStatus(ActivityStatus.PLANNED);
            // OPTION 2: tr.setStatus(ActivityStatus.CANCELLED);
        }

        trainingRepository.save(tr);
        return saved;
    }

    @Override public List<ExcursionParticipation> excursionParticipants(Long excursionId) {
        return exPartRepository.findByExcursion_ExcursionId(excursionId);
    }

    @Override public List<TrainingParticipation> trainingParticipants(Long trainingId) {
        return trPartRepository.findByTraining_TrainingId(trainingId);
    }
}
