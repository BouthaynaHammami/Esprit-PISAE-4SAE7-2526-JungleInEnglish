package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Child;

import java.util.List;

public interface IChildService {
    Child addChild(Child child);
    Child updateChild(Child child);
    void deleteChild(Long id);
    Child getChild(Long id);
    List<Child> getAllChildren();
    List<Child> getChildrenByParent(Long parentId);
    List<Child> getChildrenByLevel(Long levelId);
}
