package tn.esprit.employee.Services.IServices;

import tn.esprit.employee.Dto.UserDTO;
import tn.esprit.employee.Entities.Interview;

import java.util.List;

public interface IInterviewService {

    List<Interview> getAll();

    Interview getById(Long id);

    Interview create(Interview interview);

    Interview update(Long id, Interview interview);

    void delete(Long id);
    List<Interview> getByRecruitmentId(Long recruitmentId);
    List<Interview> getByUserId(Long userId);
    UserDTO getUserOfInterview(Long interviewId);
}
