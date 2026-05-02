package tn.esprit.LevelTest.Services.ImplServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.LevelTest.Entities.Subject;
import tn.esprit.LevelTest.Repositories.SubjectRepository;
import tn.esprit.LevelTest.Services.IServices.ISubjectService;


import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements ISubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject update(Long id, Subject subject) {
        subject.setId(id);
        return subjectRepository.save(subject);
    }

    @Override
    public void delete(Long id) {
        subjectRepository.deleteById(id);
    }

}
