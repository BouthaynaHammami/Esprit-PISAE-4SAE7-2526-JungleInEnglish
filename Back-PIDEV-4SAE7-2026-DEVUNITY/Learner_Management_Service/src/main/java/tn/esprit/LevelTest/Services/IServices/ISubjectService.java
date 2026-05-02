package tn.esprit.LevelTest.Services.IServices;

import tn.esprit.LevelTest.Entities.Subject;

import java.util.List;

public interface ISubjectService {

    List<Subject> getAll();

    Subject getById(Long id);

    Subject create(Subject subject);

    Subject update(Long id, Subject subject);

    void delete(Long id);

}
