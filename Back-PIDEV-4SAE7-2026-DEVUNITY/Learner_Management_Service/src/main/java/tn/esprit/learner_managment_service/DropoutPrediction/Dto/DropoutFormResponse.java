package tn.esprit.learner_managment_service.DropoutPrediction.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DropoutFormResponse extends DropoutFormBaseDto {

    private Long id;
    private Integer userId;
    private String userEmail;
    private String predictedDropout;
    private Double predictedProbability;
    private String modelName;
    private LocalDateTime createdAt;
}
