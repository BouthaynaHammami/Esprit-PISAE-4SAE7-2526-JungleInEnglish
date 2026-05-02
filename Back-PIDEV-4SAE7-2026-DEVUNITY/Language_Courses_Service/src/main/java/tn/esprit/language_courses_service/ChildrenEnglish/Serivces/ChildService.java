package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.ChildRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildService implements IChildService {
    
    private final ChildRepository childRepository;
    
    @Override
    public Child addChild(Child child) {
        return childRepository.save(child);
    }
    
    @Override
    public Child updateChild(Child child) {
        return childRepository.save(child);
    }
    
    @Override
    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }
    
    @Override
    public Child getChild(Long id) {
        return childRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }
    
    @Override
    public List<Child> getChildrenByParent(Long parentId) {
        return childRepository.findByParent_ParentId(parentId);
    }
    
    @Override
    public List<Child> getChildrenByLevel(Long levelId) {
        return childRepository.findByLevelChildren_LevelId(levelId);
    }
}
