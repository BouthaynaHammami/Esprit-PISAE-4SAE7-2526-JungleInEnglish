package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Parent;

import java.util.List;

public interface IParentService {
    Parent addParent(Parent parent);
    Parent updateParent(Parent parent);
    void deleteParent(Long id);
    Parent getParent(Long id);
    List<Parent> getAllParents();
    Parent getParentByUserId(Integer userId);
}
