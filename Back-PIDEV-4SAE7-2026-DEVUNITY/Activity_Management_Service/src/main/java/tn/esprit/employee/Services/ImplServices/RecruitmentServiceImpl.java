package tn.esprit.employee.Services.ImplServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.employee.Entities.Recruitment;
import tn.esprit.employee.Repositories.RecruitmentRepository;
import tn.esprit.employee.Services.IServices.IRecruitmentService;


import tn.esprit.employee.Services.IServices.IApplicantService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentServiceImpl implements IRecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final IApplicantService applicantService;

    @Override
    public List<Recruitment> getAll() {
        List<Recruitment> list = recruitmentRepository.findAll();
        // Optional: populate names for all applicants in all recruitments 
        // list.forEach(r -> { if(r.getApplicants() != null) r.getApplicants().forEach(applicantService::populateNames); });
        return list;
    }

    @Override
    public Recruitment getById(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id).orElse(null);
        if (recruitment != null && recruitment.getApplicants() != null) {
            recruitment.getApplicants().forEach(applicantService::populateNames);
        }
        return recruitment;
    }

    @Override
    public Recruitment create(Recruitment recruitment) {
        return recruitmentRepository.save(recruitment);
    }

    @Override
    public Recruitment update(Long id, Recruitment recruitment) {
        recruitment.setId(id);
        return recruitmentRepository.save(recruitment);
    }

    @Override
    public void delete(Long id) {
        recruitmentRepository.deleteById(id);
    }
}
