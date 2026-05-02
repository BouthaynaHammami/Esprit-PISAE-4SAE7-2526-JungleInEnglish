package tn.esprit.academic_management_service.Certifications.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.academic_management_service.Certifications.DTO.AnswerDTO;
import tn.esprit.academic_management_service.Certifications.DTO.TestResultDTO;
import tn.esprit.academic_management_service.Certifications.Entities.Certificate;
import tn.esprit.academic_management_service.Certifications.Entities.CertificationQuestion;
import tn.esprit.academic_management_service.Certifications.Entities.TestSession;
import tn.esprit.academic_management_service.Certifications.Repositories.CertificateRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.QuestionRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.SessionQuestionRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.TestSessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestSessionRepository sessionRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private SessionQuestionRepository sessionQuestionRepository;

    @InjectMocks
    private TestService testService;

    @Test
    void hasActiveSession_shouldReturnFalseWhenNoSession() {
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(1L)).thenReturn(Optional.empty());

        assertFalse(testService.hasActiveSession(1L));
    }

    @Test
    void hasActiveSession_shouldReturnTrueWhenSessionNotExpired() {
        TestSession session = new TestSession();
        session.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(1L)).thenReturn(Optional.of(session));

        assertTrue(testService.hasActiveSession(1L));
    }

    @Test
    void startSession_shouldPersistSessionWhenAllowed() {
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(2L)).thenReturn(Optional.empty());
        when(sessionRepository.countByStudentIdAndScoreIsNotNull(2L)).thenReturn(1);

        testService.startSession(2L);

        ArgumentCaptor<TestSession> captor = ArgumentCaptor.forClass(TestSession.class);
        verify(sessionRepository).save(captor.capture());
        TestSession saved = captor.getValue();
        assertEquals(2L, saved.getStudentId());
        assertEquals(2, saved.getAttemptNumber());
        assertEquals(false, saved.getSuspicious());
        assertEquals(0, saved.getTabSwitchCount());
        assertNotNull(saved.getTakenAt());
        assertNotNull(saved.getExpiresAt());
    }

    @Test
    void startSession_shouldDoNothingWhenActiveSessionExists() {
        TestSession active = new TestSession();
        active.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(3L)).thenReturn(Optional.of(active));

        testService.startSession(3L);

        verify(sessionRepository, never()).save(any(TestSession.class));
        verify(sessionRepository, never()).countByStudentIdAndScoreIsNotNull(3L);
    }

    @Test
    void startSession_shouldThrowWhenMaxAttemptsReached() {
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(4L)).thenReturn(Optional.empty());
        when(sessionRepository.countByStudentIdAndScoreIsNotNull(4L)).thenReturn(3);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> testService.startSession(4L));
        assertEquals("Maximum attempts reached", ex.getMessage());
        verify(sessionRepository, never()).save(any(TestSession.class));
    }

    @Test
    void registerTabViolation_shouldFlagSuspiciousOnSecondViolation() {
        TestSession session = new TestSession();
        session.setTabSwitchCount(1);
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(10L)).thenReturn(Optional.of(session));

        testService.registerTabViolation(10L);

        assertEquals(2, session.getTabSwitchCount());
        assertTrue(session.getSuspicious());
        assertFalse(session.getPassed());
        assertEquals("Multiple tab switches detected", session.getSuspiciousReason());
        verify(sessionRepository).save(session);
    }

    @Test
    void submitTest_shouldThrowWhenSessionAlreadySuspicious() {
        TestSession session = new TestSession();
        session.setSuspicious(true);
        session.setSuspiciousReason("fraud suspected");
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(11L)).thenReturn(Optional.of(session));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> testService.submitTest(11L, List.of()));

        assertEquals("Exam blocked: fraud suspected", ex.getMessage());
    }

    @Test
    void submitTest_shouldPassAndGenerateCertificate() {
        TestSession session = new TestSession();
        session.setId(99L);
        session.setTakenAt(LocalDateTime.now().minusMinutes(5));
        session.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        session.setSuspicious(false);

        CertificationQuestion q1 = new CertificationQuestion();
        q1.setId(1L);
        q1.setPoints(50);
        q1.setCorrectAnswer("A");

        CertificationQuestion q2 = new CertificationQuestion();
        q2.setId(2L);
        q2.setPoints(50);
        q2.setCorrectAnswer("B");

        AnswerDTO a1 = new AnswerDTO();
        a1.setQuestionId(1L);
        a1.setSelectedAnswer("A");
        AnswerDTO a2 = new AnswerDTO();
        a2.setQuestionId(2L);
        a2.setSelectedAnswer("B");

        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(12L)).thenReturn(Optional.of(session));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(q1));
        when(questionRepository.findById(2L)).thenReturn(Optional.of(q2));

        TestResultDTO result = testService.submitTest(12L, List.of(a1, a2));

        assertEquals(100, result.getScore());
        assertTrue(result.getPassed());
        assertEquals(100, result.getEarnedPoints());
        assertEquals(100, result.getTotalPoints());
        assertEquals("C2", result.getLevel());
        assertNotNull(result.getCertificateNumber());
        assertNotNull(result.getQrCode());
        assertTrue(result.getMessage().contains("Congratulations"));

        verify(sessionQuestionRepository).saveAll(anyList());
        verify(sessionRepository).save(session);
        verify(certificateRepository).save(any(Certificate.class));
    }

    @Test
    void submitTest_shouldFailAsSuspiciousWhenTooFast() {
        TestSession session = new TestSession();
        session.setTakenAt(LocalDateTime.now().minusSeconds(20));
        session.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        session.setSuspicious(false);

        CertificationQuestion q = new CertificationQuestion();
        q.setId(5L);
        q.setPoints(100);
        q.setCorrectAnswer("A");

        AnswerDTO answer = new AnswerDTO();
        answer.setQuestionId(5L);
        answer.setSelectedAnswer("A");

        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(13L)).thenReturn(Optional.of(session));
        when(questionRepository.findById(5L)).thenReturn(Optional.of(q));

        TestResultDTO result = testService.submitTest(13L, List.of(answer));

        assertFalse(result.getPassed());
        assertTrue(result.getMessage().contains("suspicious"));
        assertTrue(session.getSuspicious());
        assertTrue(session.getSuspiciousReason().contains("Completed too fast"));
        verify(certificateRepository, never()).save(any(Certificate.class));
    }

    @Test
    void getCertificatesForStudent_shouldReturnRepositoryResult() {
        Certificate certificate = new Certificate();
        certificate.setStudentId(20L);
        when(certificateRepository.findByStudentId(20L)).thenReturn(List.of(certificate));

        List<Certificate> result = testService.getCertificatesForStudent(20L);

        assertEquals(1, result.size());
        assertEquals(20L, result.get(0).getStudentId());
    }

    @Test
    void getSuspiciousSessions_shouldDelegateToRepository() {
        TestSession suspicious = new TestSession();
        suspicious.setSuspicious(true);
        when(sessionRepository.findBySuspiciousTrue()).thenReturn(List.of(suspicious));

        List<TestSession> result = testService.getSuspiciousSessions();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getSuspicious());
    }

    @Test
    void getAllSessions_shouldMapAllExpectedFields() {
        TestSession session = new TestSession();
        session.setId(30L);
        session.setStudentId(100L);
        session.setScore(85);
        session.setPassed(true);
        session.setSuspicious(false);
        session.setTabSwitchCount(0);
        session.setDurationSeconds(300);
        session.setTakenAt(LocalDateTime.now().minusMinutes(5));
        session.setExpiresAt(LocalDateTime.now().plusMinutes(20));

        when(sessionRepository.findAll()).thenReturn(List.of(session));

        List<Map<String, Object>> result = testService.getAllSessions();

        assertEquals(1, result.size());
        assertEquals(30L, result.get(0).get("id"));
        assertEquals(100L, result.get(0).get("studentId"));
        assertEquals(85, result.get(0).get("score"));
        assertEquals(true, result.get(0).get("passed"));
    }

    @Test
    void getAllCertificates_shouldMapAllExpectedFields() {
        Certificate certificate = new Certificate();
        certificate.setId(40L);
        certificate.setCertificateNumber("CERT-ABCD1234");
        certificate.setStudentId(200L);
        certificate.setSessionId(300L);
        certificate.setLevel("B2");
        certificate.setScore(82);
        certificate.setQrCode("qr");
        certificate.setIssuedAt(LocalDateTime.now());

        when(certificateRepository.findAll()).thenReturn(List.of(certificate));

        List<Map<String, Object>> result = testService.getAllCertificates();

        assertEquals(1, result.size());
        assertEquals("CERT-ABCD1234", result.get(0).get("certificateNumber"));
        assertEquals(200L, result.get(0).get("studentId"));
        assertEquals("B2", result.get(0).get("level"));
    }

    @Test
    void isExamBlocked_shouldReturnTrueWhenLastSessionSuspicious() {
        TestSession last = new TestSession();
        last.setSuspicious(true);
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(50L)).thenReturn(Optional.of(last));

        assertTrue(testService.isExamBlocked(50L));
        verify(sessionRepository, never()).countByStudentIdAndScoreIsNotNull(50L);
    }

    @Test
    void isExamBlocked_shouldReturnTrueWhenMaxAttemptsReached() {
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(51L)).thenReturn(Optional.empty());
        when(sessionRepository.countByStudentIdAndScoreIsNotNull(51L)).thenReturn(3);

        assertTrue(testService.isExamBlocked(51L));
    }

    @Test
    void isExamBlocked_shouldReturnFalseWhenNotSuspiciousAndAttemptsBelowMax() {
        TestSession last = new TestSession();
        last.setSuspicious(false);
        when(sessionRepository.findTopByStudentIdOrderByTakenAtDesc(52L)).thenReturn(Optional.of(last));
        when(sessionRepository.countByStudentIdAndScoreIsNotNull(52L)).thenReturn(2);

        assertFalse(testService.isExamBlocked(52L));
    }
}
