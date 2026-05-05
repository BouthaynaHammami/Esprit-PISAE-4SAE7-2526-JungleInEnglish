package tn.esprit.LevelTest.Services.IServices;

import tn.esprit.LevelTest.Dto.CourseRecommendation;
import tn.esprit.LevelTest.Dto.LevelTestResult;

public interface ILevelTestEvaluationService {
    LevelTestResult evaluateParagraphTest(String paragraphText, String subjectTitle);
    LevelTestResult evaluateOralTest(String transcript, Integer durationSeconds);
    CourseRecommendation getCourseRecommendations(String level, Integer score);
}
