package tn.esprit.language_courses_service.Schedules.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.language_courses_service.Schedules.Entities.ClassEntity;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByLevel(String level);
    Optional<ClassEntity> findByNameIgnoreCase(String name);
}