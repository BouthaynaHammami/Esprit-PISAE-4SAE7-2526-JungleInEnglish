package tn.esprit.employee.Services.IServices;


import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Applicant;

import java.util.List;

public interface IApplicantService {

    List<Applicant> getAll();

    Applicant getById(Long id);

    Applicant create(Applicant applicant);

    Applicant update(Long id, Applicant applicant);

    void delete(Long id);
    
    // ─── Filtres ───────────────────────────────────
    List<Applicant> getByUserId(Long userId);
    List<Applicant> getByRecruitmentId(Long recruitmentId);
    List<Applicant> getByInterviewId(Long interviewId);

    // ─── Feign vers User Service ───────────────────
    List<UserDTO> getAllUsers();
    UserDTO getUserOfApplicant(Long applicantId);
    void populateNames(Applicant applicant);

}
