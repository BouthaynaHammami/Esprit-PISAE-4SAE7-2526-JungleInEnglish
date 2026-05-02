package tn.esprit.language_courses_service.ChallengesCompetitions.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Challenge;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.ChallengeType;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.Level;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findFirstByTypeAndLevel(ChallengeType type, Level level);

    /**
     * Find all challenges available on a given date, optionally filtered by level and/or type.
     * If level is null, it will NOT be filtered.
     * If type is null, it will NOT be filtered.
     */
    @Query("""
        SELECT c FROM Challenge c
        WHERE (:d IS NULL OR :d IS NOT NULL)
          AND (:level IS NULL OR c.level = :level)
          AND (:type IS NULL OR c.type = :type)
        ORDER BY c.id ASC
    """)
    List<Challenge> findAvailable(@Param("d") LocalDate d,
                                  @Param("level") Level level,
                                  @Param("type") ChallengeType type);

    /**
     * Find first available challenge for a specific type and level.
     * Both type and level are required (non-null).
     */
    @Query("""
        SELECT c FROM Challenge c
        WHERE (:d IS NULL OR :d IS NOT NULL)
          AND c.level = :level
          AND c.type = :type
        ORDER BY c.id ASC
        LIMIT 1
    """)
    Optional<Challenge> findFirstAvailableByTypeAndLevel(@Param("d") LocalDate d,
                                                          @Param("type") ChallengeType type,
                                                          @Param("level") Level level);

    @Query("""
        SELECT c FROM Challenge c
        WHERE (:d IS NULL OR :d IS NOT NULL)
          AND c.level = :level
          AND c.type = :type
        ORDER BY c.id ASC
    """)
    Optional<Challenge> findFirstAvailableNow(@Param("d") LocalDate d,
                                              @Param("type") ChallengeType type,
                                              @Param("level") Level level);
}
