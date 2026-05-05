from __future__ import annotations

import logging
import os

from fastapi import APIRouter, FastAPI, HTTPException

from app.eureka_client import init_eureka
from app.model_loader import ModelArtifacts, load_artifacts, prepare_features
from app.schemas import DropoutPredictionRequest, DropoutPredictionResponse
from app.cv_analysis import analyze_cv_text
from app.level_test_evaluator import evaluate_paragraph_test, evaluate_oral_test
from app.course_recommender import recommend_courses
from pydantic import BaseModel

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("ml_service")

MODEL_PATH = os.getenv("MODEL_PATH", "models/churn_prediction.joblib")
APP_NAME = os.getenv("EUREKA_APP_NAME", "ml-service")
SERVICE_PORT = int(os.getenv("SERVICE_PORT", "8090"))

app = FastAPI(title="ML Service", version="1.0.0")
router = APIRouter(prefix="/ml/api")

artifacts: ModelArtifacts | None = None


# CV Analysis schemas
class CVAnalysisRequest(BaseModel):
    cvText: str
    skills: str
    experienceYears: int


class CVAnalysisResponse(BaseModel):
    cluster: str
    decision: str
    raison: str
    score: int
    model: str


# Level Test schemas
class ParagraphTestRequest(BaseModel):
    paragraphText: str
    subjectTitle: str


class OralTestRequest(BaseModel):
    transcript: str
    durationSeconds: int = 60


class LevelTestResponse(BaseModel):
    level: str
    score: int
    feedback: str
    details: dict


# Course Recommendation schemas
class CourseRecommendationRequest(BaseModel):
    level: str
    score: int


class CourseRecommendationResponse(BaseModel):
    current_level: str
    level_name: str
    score: int
    recommended_courses: list
    skills_to_improve: list
    next_level_suggestion: dict | None
    learning_path: dict


@app.on_event("startup")
async def startup_event() -> None:
    global artifacts
    artifacts = load_artifacts(MODEL_PATH)
    await init_eureka(APP_NAME, SERVICE_PORT)


@router.get("/health")
def health_check() -> dict:
    return {"status": "ok", "model_loaded": artifacts is not None}


@router.post("/predict", response_model=DropoutPredictionResponse)
def predict(request: DropoutPredictionRequest) -> DropoutPredictionResponse:
    if artifacts is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    try:
        features = prepare_features(request.model_dump(), artifacts)
    except ValueError as exc:
        raise HTTPException(status_code=400, detail=str(exc)) from exc

    model = artifacts.model
    probability = None

    if hasattr(model, "predict_proba"):
        prob_values = model.predict_proba(features)
        probability = float(prob_values[0][1])
        if artifacts.threshold is not None:
            prediction = int(probability >= artifacts.threshold)
        else:
            prediction = int(model.predict(features)[0])
    else:
        prediction = int(model.predict(features)[0])

    label = artifacts.label_mapping.get(prediction, str(prediction))
    return DropoutPredictionResponse(
        dropout=label,
        probability=probability,
        model=artifacts.model_name,
    )


@router.post("/analyze-cv", response_model=CVAnalysisResponse)
def analyze_cv(request: CVAnalysisRequest) -> CVAnalysisResponse:
    """Analyze CV text and return hiring decision."""
    try:
        result = analyze_cv_text(
            cv_text=request.cvText,
            skills=request.skills,
            experience_years=request.experienceYears
        )
        return CVAnalysisResponse(**result)
    except Exception as exc:
        logger.error(f"CV analysis error: {exc}")
        raise HTTPException(status_code=500, detail=str(exc)) from exc


@router.post("/evaluate-paragraph", response_model=LevelTestResponse)
def evaluate_paragraph(request: ParagraphTestRequest) -> LevelTestResponse:
    """Evaluate paragraph writing test and return CEFR level."""
    try:
        result = evaluate_paragraph_test(
            paragraph_text=request.paragraphText,
            subject_title=request.subjectTitle
        )
        return LevelTestResponse(**result)
    except Exception as exc:
        logger.error(f"Paragraph evaluation error: {exc}")
        raise HTTPException(status_code=500, detail=str(exc)) from exc


@router.post("/evaluate-oral", response_model=LevelTestResponse)
def evaluate_oral(request: OralTestRequest) -> LevelTestResponse:
    """Evaluate oral test transcript and return CEFR level."""
    try:
        result = evaluate_oral_test(
            transcript=request.transcript,
            duration_seconds=request.durationSeconds
        )
        return LevelTestResponse(**result)
    except Exception as exc:
        logger.error(f"Oral evaluation error: {exc}")
        raise HTTPException(status_code=500, detail=str(exc)) from exc


@router.post("/recommend-courses", response_model=CourseRecommendationResponse)
def get_course_recommendations(request: CourseRecommendationRequest) -> CourseRecommendationResponse:
    """Get personalized course recommendations based on student level and score."""
    try:
        result = recommend_courses(
            level=request.level,
            score=request.score
        )
        return CourseRecommendationResponse(**result)
    except Exception as exc:
        logger.error(f"Course recommendation error: {exc}")
        raise HTTPException(status_code=500, detail=str(exc)) from exc


app.include_router(router)
