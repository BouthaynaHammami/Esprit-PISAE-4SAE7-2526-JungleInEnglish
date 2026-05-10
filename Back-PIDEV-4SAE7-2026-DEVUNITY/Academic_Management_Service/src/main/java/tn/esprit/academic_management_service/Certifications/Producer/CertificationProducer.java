package tn.esprit.academic_management_service.Certifications.Producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tn.esprit.academic_management_service.Certifications.DTO.CertificateDTO;
import tn.esprit.academic_management_service.Learning.Config.RabbitMQConfig;
import tn.esprit.academic_management_service.Certifications.Repositories.CertificateRepository;
import tn.esprit.academic_management_service.Certifications.Entities.Certificate;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final CertificateRepository certificateRepository;

    public void sendCertificateEvent(CertificateDTO certificateDTO) {
        log.info("Sending Certificate event to RabbitMQ for: {}", certificateDTO.getCertificateNumber());
        rabbitTemplate.convertAndSend(RabbitMQConfig.CERTIFICATION_QUEUE, certificateDTO);
    }

    public void syncAllCertifications() {
        log.info("Synchronizing all existing certifications to Elasticsearch...");
        List<Certificate> certificates = certificateRepository.findAll();
        for (Certificate cert : certificates) {
            CertificateDTO dto = CertificateDTO.builder()
                    .id(cert.getId())
                    .studentId(cert.getStudentId())
                    .sessionId(cert.getSessionId())
                    .certificateNumber(cert.getCertificateNumber())
                    .level(cert.getLevel())
                    .score(cert.getScore())
                    .issuedAt(Date.from(cert.getIssuedAt().atZone(ZoneId.systemDefault()).toInstant()))
                    .build();
            sendCertificateEvent(dto);
        }
        log.info("Synchronization of {} certifications completed.", certificates.size());
    }
}
