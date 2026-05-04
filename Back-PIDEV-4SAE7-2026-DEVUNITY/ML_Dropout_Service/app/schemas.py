from pydantic import BaseModel, Field


class DropoutPredictionRequest(BaseModel):
    motivation_level: int = Field(..., ge=0)
    weekly_study_hours: float = Field(..., ge=0)
    free_time_hours_per_week: float = Field(..., ge=0)
    satisfaction_level: int = Field(..., ge=0)
    preferred_learning_mode: str
    attendance_commitment: int = Field(..., ge=0)
    homework_completion_self: int = Field(..., ge=0)
    financial_stress_level: int = Field(..., ge=0)
    interaction_with_teacher: int = Field(..., ge=0)
    english_level_self: str
    goal_clarity_level: int = Field(..., ge=0)
    class_difficulty_level: int = Field(..., ge=0)
    peer_interaction_level: int = Field(..., ge=0)
    technical_issues_frequency: int = Field(..., ge=0)


class DropoutPredictionResponse(BaseModel):
    dropout: str
    probability: float | None
    model: str
