package tn.esprit.learner_managment_service.DropoutPrediction.Services;

import java.util.List;

import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormResponse;

public interface DropoutFormService {
    DropoutFormResponse createForm(DropoutFormRequest request);
    List<DropoutFormResponse> getAllForms();
    DropoutFormResponse getFormById(Long id);
    List<DropoutFormResponse> getFormsByUserId(Integer userId);
}
