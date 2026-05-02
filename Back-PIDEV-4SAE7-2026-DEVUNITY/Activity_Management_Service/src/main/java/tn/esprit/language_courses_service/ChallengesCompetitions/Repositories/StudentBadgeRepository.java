package tn.esprit.language_courses_service.ChallengesCompetitions.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChallengesCompetitions.Entities.StudentBadge;

import java.util.List;
import java.util.Optional;

/**
 * Repository for StudentBadge entity
 * Manages persistence operations for student-badge relationships
 */
@Repository
public interface StudentBadgeRepository extends JpaRepository<StudentBadge, Long> {

    /**
     * Find all badges earned by a student
     * 
     * @param idUser - Student ID
     * @return List of StudentBadge records for this student
     */
    List<StudentBadge> findByIdUserOrderByDateAcquisitionDesc(Long idUser);

    /**
     * Check if a student already has a specific badge
     * 
     * @param idUser - Student ID
     * @param idBadge - Badge ID
     * @return Optional containing the StudentBadge if exists
     */
    Optional<StudentBadge> findByIdUserAndBadge_IdBadge(Long idUser, Long idBadge);

    /**
     * Count badges earned by a student
     * 
     * @param idUser - Student ID
     * @return Number of badges
     */
    long countByIdUser(Long idUser);

    /**
     * Find badges earned by a student in a date range
     * Useful for analytics
     * 
     * @param idUser - Student ID
     * @return List of recent StudentBadges
     */
    @Query(value = "SELECT sb FROM StudentBadge sb WHERE sb.idUser = :idUser ORDER BY sb.dateAcquisition DESC LIMIT :limit")
    List<StudentBadge> findRecentBadgesForUser(@Param("idUser") Long idUser, @Param("limit") int limit);

    /**
     * Check if a student has earned at least N badges
     * 
     * @param idUser - Student ID
     * @param count - Minimum badge count
     * @return true if student has >= count badges
     */
    @Query(value = "SELECT COUNT(*) >= :count FROM student_badges WHERE idUser = :idUser", nativeQuery = true)
    boolean hasAtLeastNBadges(@Param("idUser") Long idUser, @Param("count") int count);

    /**
     * Find all badges earned by students for a specific badge type
     * Useful for analytics
     * 
     * @param idBadge - Badge ID
     * @return List of StudentBadge records
     */
    List<StudentBadge> findByBadge_IdBadge(Long idBadge);
}
