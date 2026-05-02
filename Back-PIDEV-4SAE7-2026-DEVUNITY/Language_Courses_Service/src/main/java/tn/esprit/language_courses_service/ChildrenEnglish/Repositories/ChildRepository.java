package tn.esprit.language_courses_service.ChildrenEnglish.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByParent_ParentId(Long parentId);
    List<Child> findByLevelChildren_LevelId(Long levelId);
}
