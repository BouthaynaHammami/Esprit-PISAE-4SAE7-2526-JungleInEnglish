package tn.esprit.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.Services.IServices.IActivityService;
import tn.esprit.Books_Clubs.entities.*;
import tn.esprit.repositories.*;

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
    private final BookClubUserRepository BookClubUserRepository;

    @Override
    public Excursion addExcursion(Excursion e) {
        if (e.getClub() == null || e.getClub().getClubId() == null) {
            throw new IllegalArgumentException("Club is required");
        }

        Club club = clubRepository.findById(e.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        e.setClub(club);

        if (e.getStatus() == null) {
            e.setStatus(ActivityStatus.PLANNED);
        }
        if (e.getNbrDeReservation() == null) {
            e.setNbrDeReservation(0);
        }

        return excursionRepository.save(e);
    }

    @Override
    public Training addTraining(Training t) {
        if (t.getClub() == null || t.getClub().getClubId() == null) {
            throw new IllegalArgumentException("Club is required");
        }

        Club club = clubRepository.findById(t.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        t.setClub(club);

        if (t.getStatus() == null) {
            t.setStatus(ActivityStatus.PLANNED);
        }
        if (t.getNbrDeReservation() == null) {
            t.setNbrDeReservation(0);
        }

        return trainingRepository.save(t);
    }

    @Override
    public Excursion updateExcursion(Excursion e) {
        if (e.getExcursionId() == null) {
            throw new IllegalArgumentException("Excursion id is required");
        }

        excursionRepository.findById(e.getExcursionId())
                .orElseThrow(() -> new IllegalArgumentException("Excursion not found"));

        if (e.getClub() != null && e.getClub().getClubId() != null) {
            Club club = clubRepository.findById(e.getClub().getClubId())
                    .orElseThrow(() -> new IllegalArgumentException("Club not found"));
            e.setClub(club);
        }

        return excursionRepository.save(e);
    }

    @Override
    public Training updateTraining(Training t) {
        if (t.getTrainingId() == null) {
            throw new IllegalArgumentException("Training id is required");
        }

        trainingRepository.findById(t.getTrainingId())
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));

        if (t.getClub() != null && t.getClub().getClubId() != null) {
            Club club = clubRepository.findById(t.getClub().getClubId())
                    .orElseThrow(() -> new IllegalArgumentException("Club not found"));
            t.setClub(club);
        }

        return trainingRepository.save(t);
    }

    @Override
    public void deleteExcursion(Long id) {
        excursionRepository.deleteById(id);
    }

    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

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

    @Override
    public List<Excursion> excursionsByClub(Long clubId) {
        return excursionRepository.findByClub_ClubId(clubId);
    }

    @Override
    public List<Training> trainingsByClub(Long clubId) {
        return trainingRepository.findByClub_ClubId(clubId);
    }

    @Override
    @Transactional
    public ExcursionParticipation registerExcursion(Long memberId, Long excursionId) {
        if (exPartRepository.existsByMember_UserIdAndExcursion_ExcursionId(memberId.intValue(), excursionId)) {
            throw new IllegalStateException("Already registered");
        }

        Excursion ex = excursionRepository.findById(excursionId)
                .orElseThrow(() -> new IllegalArgumentException("Excursion not found"));

        if (ex.getStatus() == ActivityStatus.CANCELLED || ex.getStatus() == ActivityStatus.FINISHED) {
            throw new IllegalStateException("This excursion is not available for reservation");
        }

        int reserved = ex.getNbrDeReservation() == null ? 0 : ex.getNbrDeReservation();
        int capacity = ex.getNbrDeplace() == null ? 0 : ex.getNbrDeplace();

        if (capacity <= 0 || reserved >= capacity) {
            throw new IllegalStateException("No places available");
        }

        User member = BookClubUserRepository.findById(memberId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ExcursionParticipation p = new ExcursionParticipation();
        p.setMember(member);
        p.setExcursion(ex);
        p.setRegistrationDate(LocalDate.now());
        p.setStatus(ParticipationStatus.REGISTERED);

        ExcursionParticipation saved = exPartRepository.save(p);

        ex.setNbrDeReservation(reserved + 1);
        excursionRepository.save(ex);

        return saved;
    }

    @Override
    @Transactional
    public TrainingParticipation registerTraining(Long memberId, Long trainingId) {
        if (trPartRepository.existsByMember_UserIdAndTraining_TrainingId(memberId.intValue(), trainingId)) {
            throw new IllegalStateException("Already registered");
        }

        Training tr = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found"));

        if (tr.getStatus() == ActivityStatus.CANCELLED || tr.getStatus() == ActivityStatus.FINISHED) {
            throw new IllegalStateException("This training is not available for reservation");
        }

        int reserved = tr.getNbrDeReservation() == null ? 0 : tr.getNbrDeReservation();
        int capacity = tr.getNbrDeplace() == null ? 0 : tr.getNbrDeplace();

        if (capacity <= 0 || reserved >= capacity) {
            throw new IllegalStateException("No places available");
        }

        User member = BookClubUserRepository.findById(memberId.intValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TrainingParticipation p = new TrainingParticipation();
        p.setMember(member);
        p.setTraining(tr);
        p.setRegistrationDate(LocalDate.now());
        p.setStatus(ParticipationStatus.REGISTERED);
        p.setCompleted(false);
        p.setRewardTransferred(false);

        TrainingParticipation saved = trPartRepository.save(p);

        tr.setNbrDeReservation(reserved + 1);
        trainingRepository.save(tr);

        return saved;
    }

    @Override
    public List<ExcursionParticipation> excursionParticipants(Long excursionId) {
        return exPartRepository.findByExcursion_ExcursionId(excursionId);
    }

    @Override
    public List<TrainingParticipation> trainingParticipants(Long trainingId) {
        return trPartRepository.findByTraining_TrainingId(trainingId);
    }
}