package tn.esprit.academic_management_service.Certifications.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Certifications.DTO.QuestionDTO;
import tn.esprit.academic_management_service.Certifications.Entities.CertificationQuestion;
import tn.esprit.academic_management_service.Certifications.Repositories.QuestionRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.TestSessionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TestSessionRepository sessionRepository;

    // ===============================
    // 🎓 STUDENT
    // ===============================
    public List<QuestionDTO> getQuestionsForStudent(Long studentId) {

        // ✅ Supprimé — tout est déjà vérifié dans startSession()
        // Plus de countByStudentId ni de checks redondants ici

        List<CertificationQuestion> questions = questionRepository.findRandomActiveQuestions();

        if (questions == null || questions.isEmpty()) {
            throw new RuntimeException("No active questions available.");
        }

        return questions.stream()
                .map(this::toDTO)
                .toList();
    }

    // ===============================
    // 👨‍💼 ADMIN
    // ===============================
    public List<CertificationQuestion> getAllActiveQuestions() {
        return questionRepository.findByActiveTrue();
    }

    public CertificationQuestion addQuestion(CertificationQuestion q) {
        q.setActive(true);
        return questionRepository.save(q);
    }

    public CertificationQuestion updateQuestion(Long id, CertificationQuestion q) {

        CertificationQuestion existing = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        existing.setQuestionText(q.getQuestionText());
        existing.setOptionA(q.getOptionA());
        existing.setOptionB(q.getOptionB());
        existing.setOptionC(q.getOptionC());
        existing.setOptionD(q.getOptionD());
        existing.setCorrectAnswer(q.getCorrectAnswer());
        existing.setLevel(q.getLevel());
        existing.setCategory(q.getCategory());
        existing.setPoints(q.getPoints());
        existing.setActive(q.getActive());

        return questionRepository.save(existing);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found");
        }
        questionRepository.deleteById(id);
    }

    // ===============================
    // 🔁 Mapper
    // ===============================
    private QuestionDTO toDTO(CertificationQuestion q) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(q.getId());
        dto.setQuestionText(q.getQuestionText());
        dto.setOptionA(q.getOptionA());
        dto.setOptionB(q.getOptionB());
        dto.setOptionC(q.getOptionC());
        dto.setOptionD(q.getOptionD());
        dto.setLevel(q.getLevel());
        dto.setCategory(q.getCategory());
        dto.setPoints(q.getPoints());
        return dto;
    }
}