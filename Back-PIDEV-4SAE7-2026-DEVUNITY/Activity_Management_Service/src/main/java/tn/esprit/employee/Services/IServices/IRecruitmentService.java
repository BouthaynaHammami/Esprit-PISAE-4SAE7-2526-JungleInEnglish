package tn.esprit.employee.Services.IServices;

import tn.esprit.employee.Entities.Recruitment;

import java.util.List;

public interface IRecruitmentService {

    List<Recruitment> getAll();

    Recruitment getById(Long id);

    Recruitment create(Recruitment recruitment);

    Recruitment update(Long id, Recruitment recruitment);

    void delete(Long id);
}
