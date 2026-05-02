package tn.esprit.academic_management_service.Certifications.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.academic_management_service.Certifications.DTO.AnswerDTO;
import tn.esprit.academic_management_service.Certifications.DTO.TestResultDTO;
import tn.esprit.academic_management_service.Certifications.Entities.Certificate;
import tn.esprit.academic_management_service.Certifications.Entities.CertificationQuestion;
import tn.esprit.academic_management_service.Certifications.Entities.SessionQuestion;
import tn.esprit.academic_management_service.Certifications.Entities.TestSession;
import tn.esprit.academic_management_service.Certifications.Repositories.CertificateRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.QuestionRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.SessionQuestionRepository;
import tn.esprit.academic_management_service.Certifications.Repositories.TestSessionRepository;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TestService {

    private final QuestionRepository questionRepository;
    private final TestSessionRepository sessionRepository;
    private final CertificateRepository certificateRepository;
    private final SessionQuestionRepository sessionQuestionRepository;

    private static final int MAX_ATTEMPTS = 3;
    private static final int PASS_SCORE = 70;
    private static final int MAX_DURATION_MINUTES = 30;

    // ================= ACTIVE SESSION =================

    public boolean hasActiveSession(Long studentId) {

        Optional<TestSession> session =
                sessionRepository.findTopByStudentIdOrderByTakenAtDesc(studentId);

        if (session.isEmpty()) return false;

        TestSession s = session.get();

        return s.getExpiresAt() != null &&
                LocalDateTime.now().isBefore(s.getExpiresAt());
    }

    // ================= START SESSION =================

    public void startSession(Long studentId) {

        if (hasActiveSession(studentId)) return;

        int attempts = sessionRepository.countByStudentIdAndScoreIsNotNull(studentId);

        if (attempts >= MAX_ATTEMPTS) {
            throw new RuntimeException("Maximum attempts reached");
        }

        TestSession session = new TestSession();

        session.setStudentId(studentId);
        session.setTakenAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(MAX_DURATION_MINUTES));
        session.setAttemptNumber(attempts + 1);
        session.setSuspicious(false);
        session.setTabSwitchCount(0);

        sessionRepository.save(session);
    }

    // ================= TAB SWITCH =================

    public void registerTabViolation(Long studentId) {

        TestSession session = sessionRepository
                .findTopByStudentIdOrderByTakenAtDesc(studentId)
                .orElseThrow(() -> new RuntimeException("No active session"));

        int count = session.getTabSwitchCount() == null ? 0 : session.getTabSwitchCount();

        count++;

        session.setTabSwitchCount(count);

        if (count >= 2) {
            session.setSuspicious(true);
            session.setPassed(false);
            session.setSuspiciousReason("Multiple tab switches detected");
        }

        sessionRepository.save(session);
    }

    // ================= SUBMIT TEST =================

    public TestResultDTO submitTest(Long studentId, List<AnswerDTO> answers) {

        TestSession session = sessionRepository
                .findTopByStudentIdOrderByTakenAtDesc(studentId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (Boolean.TRUE.equals(session.getSuspicious())) {
            throw new RuntimeException("Exam blocked: " + session.getSuspiciousReason());
        }

        int totalPoints = 0;
        int earnedPoints = 0;

        List<SessionQuestion> sessionQuestions = new ArrayList<>();

        for (AnswerDTO answer : answers) {

            CertificationQuestion question = questionRepository
                    .findById(answer.getQuestionId())
                    .orElse(null);

            if (question == null) continue;

            totalPoints += question.getPoints();

            boolean correct =
                    question.getCorrectAnswer()
                            .equalsIgnoreCase(answer.getSelectedAnswer());

            if (correct) earnedPoints += question.getPoints();

            SessionQuestion sq = new SessionQuestion();

            sq.setSession(session);
            sq.setQuestion(question);
            sq.setStudentAnswer(answer.getSelectedAnswer());
            sq.setCorrect(correct);

            sessionQuestions.add(sq);
        }

        sessionQuestionRepository.saveAll(sessionQuestions);

        int score = totalPoints == 0 ? 0 :
                (int) ((earnedPoints * 100.0) / totalPoints);

        long durationSeconds =
                Duration.between(session.getTakenAt(), LocalDateTime.now()).getSeconds();

        session.setDurationSeconds((int) durationSeconds);
        session.setScore(score);

        boolean suspicious = false;
        List<String> reasons = new ArrayList<>();

        // expiration
        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            suspicious = true;
            reasons.add("Submitted after expiration");
        }

        // too fast
        if (durationSeconds < 60) {
            suspicious = true;
            reasons.add("Completed too fast");
        }

        session.setSuspicious(suspicious);

        if (suspicious) {
            session.setSuspiciousReason(String.join(" | ", reasons));
            session.setPassed(false);
        } else {
            session.setPassed(score >= PASS_SCORE);
        }

        sessionRepository.save(session);

        TestResultDTO result = new TestResultDTO();

        result.setScore(score);
        result.setPassed(session.getPassed());
        result.setEarnedPoints(earnedPoints);
        result.setTotalPoints(totalPoints);

        // ================= CERTIFICATE =================

        if (Boolean.TRUE.equals(session.getPassed())
                && !Boolean.TRUE.equals(session.getSuspicious())) {

            String level =
                    score >= 95 ? "C2"
                            : score >= 90 ? "C1"
                            : score >= 80 ? "B2"
                            : "B1";

            Certificate certificate = new Certificate();

            certificate.setStudentId(studentId);
            certificate.setSessionId(session.getId());
            certificate.setCertificateNumber(
                    "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
            );

            certificate.setLevel(level);
            certificate.setScore(score);
            certificate.setIssuedAt(LocalDateTime.now());
            certificate.setQrCode(generateQR(certificate.getCertificateNumber()));

            certificateRepository.save(certificate);

            result.setCertificateNumber(certificate.getCertificateNumber());
            result.setQrCode(certificate.getQrCode());
            result.setLevel(level);
            result.setMessage("Congratulations! Level " + level + " obtained.");

        } else if (Boolean.TRUE.equals(session.getSuspicious())) {

            result.setMessage("Test suspicious: " + session.getSuspiciousReason());

        } else {

            result.setMessage("Score insufficient. Minimum required: 70%");
        }

        return result;
    }

    // ================= STUDENT CERTIFICATES =================

    public List<Certificate> getCertificatesForStudent(Long studentId) {
        return certificateRepository.findByStudentId(studentId);
    }

    // ================= ADMIN =================

    public List<TestSession> getSuspiciousSessions() {
        return sessionRepository.findBySuspiciousTrue();
    }

    // ⭐ CORRIGÉ POUR ADMIN PANEL

    public List<Map<String, Object>> getAllSessions() {

        List<Map<String, Object>> list = new ArrayList<>();

        for (TestSession session : sessionRepository.findAll()) {

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("id", session.getId());
            map.put("studentId", session.getStudentId());
            map.put("score", session.getScore());
            map.put("passed", session.getPassed());
            map.put("suspicious", session.getSuspicious());
            map.put("suspiciousReason", session.getSuspiciousReason());
            map.put("tabSwitchCount", session.getTabSwitchCount());
            map.put("durationSeconds", session.getDurationSeconds());
            map.put("takenAt", session.getTakenAt());
            map.put("expiresAt", session.getExpiresAt());

            list.add(map);
        }

        return list;
    }

    public List<Map<String, Object>> getAllCertificates() {

        List<Map<String, Object>> list = new ArrayList<>();

        for (Certificate cert : certificateRepository.findAll()) {

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("id", cert.getId());
            map.put("certificateNumber", cert.getCertificateNumber());
            map.put("studentId", cert.getStudentId());
            map.put("sessionId", cert.getSessionId());
            map.put("level", cert.getLevel());
            map.put("score", cert.getScore());
            map.put("issuedAt", cert.getIssuedAt());
            map.put("qrCode", cert.getQrCode());

            list.add(map);
        }

        return list;
    }

    // ================= EXAM BLOCK =================

    public boolean isExamBlocked(Long studentId) {

        Optional<TestSession> lastSession =
                sessionRepository.findTopByStudentIdOrderByTakenAtDesc(studentId);

        if (lastSession.isPresent() &&
                Boolean.TRUE.equals(lastSession.get().getSuspicious())) {
            return true;
        }

        int attempts = sessionRepository.countByStudentIdAndScoreIsNotNull(studentId);

        return attempts >= MAX_ATTEMPTS;
    }

    // ================= QR CODE =================

    private String generateQR(String text) {

        try {

            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix =
                    writer.encode(text, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            return "data:image/png;base64," +
                    Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (Exception e) {
            return "";
        }
    }
}