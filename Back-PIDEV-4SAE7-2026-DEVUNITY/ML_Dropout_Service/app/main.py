from __future__ import annotations

import logging
import os

from fastapi import APIRouter, FastAPI, HTTPException

from app.eureka_client import init_eureka
from app.model_loader import ModelArtifacts, load_artifacts, prepare_features
from app.schemas import DropoutPredictionRequest, DropoutPredictionResponse

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("ml_dropout_service")

MODEL_PATH = os.getenv("MODEL_PATH", "models/churn_prediction.joblib")
APP_NAME = os.getenv("EUREKA_APP_NAME", "ml-dropout-service")
SERVICE_PORT = int(os.getenv("SERVICE_PORT", "8090"))

app = FastAPI(title="ML Dropout Service", version="1.0.0")
router = APIRouter(prefix="/ml/api")

artifacts: ModelArtifacts | None = None


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


app.include_router(router)
