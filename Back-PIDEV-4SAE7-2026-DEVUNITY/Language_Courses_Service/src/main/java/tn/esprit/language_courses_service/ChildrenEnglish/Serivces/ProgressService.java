package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Progress;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.ProgressRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService implements IProgressService {
    
    private final ProgressRepository progressRepository;
    private final tn.esprit.language_courses_service.ChildrenEnglish.Repositories.ChildRepository childRepository;
    
    @Override
    public Progress addProgress(Progress progress) {
        if (progress.getLastAccess() == null) {
            progress.setLastAccess(new Date());
        }
        return progressRepository.save(progress);
    }
    
    @Override
    public Progress updateProgress(Progress progress) {
        return progressRepository.save(progress);
    }
    
    @Override
    public void deleteProgress(Long id) {
        progressRepository.deleteById(id);
    }
    
    @Override
    public Progress getProgress(Long id) {
        return progressRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Progress> getAllProgress() {
        return progressRepository.findAll();
    }
    
    @Override
    public List<Progress> getProgressByChild(Long childId) {
        return progressRepository.findByChild_ChildId(childId);
    }
    
    @Override
    public List<Progress> getProgressByCourse(Long courseId) {
        return progressRepository.findByCourse_CourseId(courseId);
    }
    
    @Override
    public List<Progress> getProgressByChildAndCourse(Long childId, Long courseId) {
        return progressRepository.findByChild_ChildIdAndCourse_CourseId(childId, courseId);
    }
    
    @Override
    public tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child getChildById(Long childId) {
        return childRepository.findById(childId).orElse(null);
    }
}
