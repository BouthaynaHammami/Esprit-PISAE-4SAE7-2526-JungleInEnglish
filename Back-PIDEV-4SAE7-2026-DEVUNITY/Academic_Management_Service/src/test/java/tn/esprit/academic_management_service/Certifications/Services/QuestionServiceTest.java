package tn.esprit.academic_management_service.Certifications.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.academic_management_service.Certifications.DTO.QuestionDTO;
import tn.esprit.academic_management_service.Certifications.Entities.CertificationQuestion;
import tn.esprit.academic_management_service.Certifications.Repositories.QuestionRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.TestSessionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestSessionRepository sessionRepository;

    @InjectMocks
    private QuestionService questionService;

    private CertificationQuestion testQuestion;

    @BeforeEach
    void setUp() {
        testQuestion = new CertificationQuestion();
        testQuestion.setId(1L);
        testQuestion.setQuestionText("What is Java?");
        testQuestion.setOptionA("Language");
        testQuestion.setOptionB("IDE");
        testQuestion.setCorrectAnswer("A");
        testQuestion.setLevel("B1");
        testQuestion.setCategory("Basics");
        testQuestion.setPoints(10);
        testQuestion.setActive(true);
    }

    @Test
    void getQuestionsForStudent_shouldReturnRandomQuestions() {
        when(questionRepository.findRandomActiveQuestions()).thenReturn(List.of(testQuestion));

        List<QuestionDTO> result = questionService.getQuestionsForStudent(1L);

        assertEquals(1, result.size());
        assertEquals("What is Java?", result.get(0).getQuestionText());
    }

    @Test
    void getQuestionsForStudent_shouldThrowWhenNoQuestionsAvailable() {
        when(questionRepository.findRandomActiveQuestions()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> questionService.getQuestionsForStudent(1L));
    }

    @Test
    void getAllActiveQuestions_shouldReturnAllActive() {
        when(questionRepository.findByActiveTrue()).thenReturn(List.of(testQuestion));

        List<CertificationQuestion> result = questionService.getAllActiveQuestions();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }

    @Test
    void addQuestion_shouldSetActiveAndSave() {
        when(questionRepository.save(any(CertificationQuestion.class))).thenReturn(testQuestion);

        CertificationQuestion result = questionService.addQuestion(testQuestion);

        assertTrue(result.getActive());
        verify(questionRepository).save(testQuestion);
    }

    @Test
    void updateQuestion_shouldUpdateAllFieldsAndSave() {
        CertificationQuestion updates = new CertificationQuestion();
        updates.setQuestionText("Updated Q");
        updates.setCorrectAnswer("B");
        updates.setPoints(20);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(testQuestion));
        when(questionRepository.save(any(CertificationQuestion.class))).thenReturn(testQuestion);

        CertificationQuestion result = questionService.updateQuestion(1L, updates);

        assertNotNull(result);
        verify(questionRepository).save(any(CertificationQuestion.class));
    }

    @Test
    void deleteQuestion_shouldThrowWhenNotFound() {
        when(questionRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> questionService.deleteQuestion(999L));
    }

    @Test
    void deleteQuestion_shouldDeleteWhenExists() {
        when(questionRepository.existsById(1L)).thenReturn(true);

        questionService.deleteQuestion(1L);

        verify(questionRepository).deleteById(1L);
    }
}
