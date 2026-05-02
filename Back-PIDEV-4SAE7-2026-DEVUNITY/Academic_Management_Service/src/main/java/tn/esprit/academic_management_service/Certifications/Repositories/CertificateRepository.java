package tn.esprit.academic_management_service.Certifications.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.academic_management_service.Certifications.Entities.Certificate;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByStudentId(Long studentId);
}