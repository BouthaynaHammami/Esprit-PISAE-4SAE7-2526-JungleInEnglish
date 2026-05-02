package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Parent;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.ParentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService implements IParentService {
    
    private final ParentRepository parentRepository;
    
    @Override
    public Parent addParent(Parent parent) {
        return parentRepository.save(parent);
    }
    
    @Override
    public Parent updateParent(Parent parent) {
        return parentRepository.save(parent);
    }
    
    @Override
    public void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }
    
    @Override
    public Parent getParent(Long id) {
        return parentRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }
    
    @Override
    public Parent getParentByUserId(Integer userId) {
        return parentRepository.findByUserId(userId).orElse(null);
    }
}
