package tn.esprit.learner_managment_service.DropoutPrediction.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormRequest;
import tn.esprit.learner_managment_service.DropoutPrediction.Dto.DropoutFormResponse;
import tn.esprit.learner_managment_service.DropoutPrediction.Services.DropoutFormService;

@RestController
@RequestMapping("/dropout-forms")
@RequiredArgsConstructor
public class DropoutFormController {

    private final DropoutFormService dropoutFormService;

    @PostMapping
    public ResponseEntity<DropoutFormResponse> createForm(@RequestBody DropoutFormRequest request) throws Exception {
        DropoutFormResponse response = dropoutFormService.createForm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DropoutFormResponse>> getAllForms() {
        return ResponseEntity.ok(dropoutFormService.getAllForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DropoutFormResponse> getForm(@PathVariable Long id) {
        return ResponseEntity.ok(dropoutFormService.getFormById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DropoutFormResponse>> getFormsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(dropoutFormService.getFormsByUserId(userId));
    }
}
