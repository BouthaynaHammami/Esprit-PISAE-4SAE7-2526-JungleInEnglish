package tn.esprit.learner_managment_service.DropoutPrediction.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MlPredictResponse {

    private String dropout;
    private Double probability;
    private String model;
}
