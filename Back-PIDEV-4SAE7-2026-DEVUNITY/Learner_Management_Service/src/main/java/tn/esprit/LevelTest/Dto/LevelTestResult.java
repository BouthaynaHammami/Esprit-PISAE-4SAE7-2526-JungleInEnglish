package tn.esprit.LevelTest.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelTestResult {
    private String level;
    private Integer score;
    private String feedback;
    private Map<String, Object> details;
}
