package tn.esprit.language_courses_service.BusinessEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.BusinessEnglish.Entities.BusinessEnglishPath;

@Repository
public interface BusinessEnglishPathRepository extends JpaRepository<BusinessEnglishPath, Long> {
}
