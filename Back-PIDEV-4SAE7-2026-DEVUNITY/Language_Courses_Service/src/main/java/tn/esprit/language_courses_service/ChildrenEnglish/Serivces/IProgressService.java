package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Progress;

import java.util.List;

public interface IProgressService {
    Progress addProgress(Progress progress);
    Progress updateProgress(Progress progress);
    void deleteProgress(Long id);
    Progress getProgress(Long id);
    List<Progress> getAllProgress();
    List<Progress> getProgressByChild(Long childId);
    List<Progress> getProgressByCourse(Long courseId);
    List<Progress> getProgressByChildAndCourse(Long childId, Long courseId);
    tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child getChildById(Long childId);
}
