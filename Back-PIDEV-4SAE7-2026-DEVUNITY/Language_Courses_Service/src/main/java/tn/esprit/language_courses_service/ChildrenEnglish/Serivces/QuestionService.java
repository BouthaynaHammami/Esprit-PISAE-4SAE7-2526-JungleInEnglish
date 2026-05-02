package tn.esprit.language_courses_service.ChildrenEnglish.Serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.language_courses_service.ChildrenEnglish.Entities.Question;
import tn.esprit.language_courses_service.ChildrenEnglish.Repositories.QuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    
    public List<Question> getQuestionsByActivityId(Long activityId) {
        return questionRepository.findByActivity_ActivityId(activityId);
    }
}
