package tn.esprit.LevelTest.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.LevelTest.Entities.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
