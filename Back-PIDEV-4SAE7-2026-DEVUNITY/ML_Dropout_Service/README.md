# ML Dropout Prediction Service

🚀 **FastAPI microservice** for real-time student dropout risk prediction using K-Nearest Neighbors (KNN) classification. Integrates with Spring Cloud Eureka for service discovery and the API Gateway for request routing.

---

## 📋 Table of Contents

- [System Requirements](#system-requirements)
- [Quick Start](#quick-start)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Service](#running-the-service)
- [API Endpoints](#api-endpoints)
- [Model Artifact Format](#model-artifact-format)
- [Troubleshooting](#troubleshooting)
- [Architecture](#architecture)

---

## System Requirements

| Requirement | Version | Notes |
|---|---|---|
| Python | **3.13** (or 3.10+) | Earlier versions lack numpy 2.x support. Python 3.13+ recommended for performance. |
| pip | Latest | Run `python -m pip install --upgrade pip` |
| Memory | 1GB+ | For model loading and inference |
| Disk Space | 500MB+ | For dependencies and model artifacts |

**Optional (for local development):**
- Git
- Postman or curl for API testing
- Docker (for containerization)

---

## Quick Start

### Windows (PowerShell)
```powershell
# 1. Navigate to service directory
cd .\ML_Dropout_Service\

# 2. Install dependencies
pip install -r requirements.txt

# 3. Place your trained model
# Copy your churn_prediction.joblib to ./models/ folder

# 4. Start the service
uvicorn app.main:app --host 0.0.0.0 --port 8090
```

### macOS/Linux (Bash)
```bash
# 1. Navigate to service directory
cd ./ML_Dropout_Service/

# 2. Create virtual environment (recommended)
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# 3. Install dependencies
pip install -r requirements.txt

# 4. Place your trained model
cp /path/to/churn_prediction.joblib ./models/

# 5. Start the service
uvicorn app.main:app --host 0.0.0.0 --port 8090
```

Expected output:
```
INFO:     Uvicorn running on http://0.0.0.0:8090
INFO:     Application startup complete
```

Visit: http://localhost:8090/docs (interactive API docs)

---

## Installation & Setup

### Step 1: Verify Python Installation

```bash
# Check Python version (should be 3.10+)
python --version

# List available Python versions
py -0  # Windows
python3 --version  # macOS/Linux
```

### Step 2: Navigate to Service Directory

```powershell
cd Back-PIDEV-4SAE7-2026-DEVUNITY\ML_Dropout_Service\
```

### Step 3: Create Virtual Environment (Recommended)

**Windows:**
```powershell
python -m venv venv
.\venv\Scripts\Activate.ps1
```

**macOS/Linux:**
```bash
python3 -m venv venv
source venv/bin/activate
```

✅ Your prompt should now show `(venv)` prefix

### Step 4: Install Dependencies

```bash
pip install --upgrade pip

pip install -r requirements.txt
```

Expected installation (takes 2-5 minutes):
```
Successfully installed fastapi-0.111.0 uvicorn-0.30.1 pydantic-2.7.1 joblib-1.4.2 numpy-2.x.x pandas-2.2.2 py-eureka-client-0.11.6 scikit-learn-1.x.x
```

### Step 5: Verify Installation

```bash
python -c "import fastapi, pydantic, joblib, numpy; print('✅ All dependencies installed!')"
```

---

## Configuration

### Environment Variables

Create a `.env` file in the `ML_Dropout_Service/` directory (or copy from `.env.example`):

```env
# Eureka Registration
EUREKA_SERVER=http://localhost:8761/eureka
SERVICE_NAME=ml-dropout-service
SERVICE_PORT=8090
SERVICE_INSTANCE_ID=ml-dropout-service:8090

# Model Configuration
MODEL_PATH=./models/churn_prediction.joblib

# Logging
LOG_LEVEL=INFO
```

**For Docker/Production:**
```env
EUREKA_SERVER=http://eureka-server:8761/eureka
SERVICE_NAME=ml-dropout-service
SERVICE_PORT=8090
```

### Application Settings

Edit `app/config.py` (if it exists) or modify `app/main.py` to configure:

```python
# Model loading timeout
MODEL_LOAD_TIMEOUT = 30  # seconds

# Request validation
MAX_FEATURE_VALUE = 1000
MIN_FEATURE_VALUE = 0

# CORS settings
ALLOWED_ORIGINS = ["*"]  # Restrict in production
```

---

## Running the Service

### Development Mode

```bash
# With auto-reload on code changes
uvicorn app.main:app --reload --host 0.0.0.0 --port 8090
```

### Production Mode (Recommended)

```bash
# With worker processes for higher concurrency
uvicorn app.main:app --host 0.0.0.0 --port 8090 --workers 4
```

### With Eureka Registration

The service automatically registers with Eureka on startup. Verify registration:

```bash
# Check Eureka dashboard
curl http://localhost:8761/eureka/apps
```

### Stop the Service

```
Press Ctrl+C
```

---

## API Endpoints

### 1. Health Check

```http
GET /ml/api/health
```

**Response (200 OK):**
```json
{
  "status": "ok",
  "model_loaded": true,
  "service_version": "1.0.0",
  "timestamp": "2026-05-04T10:30:00Z"
}
```

### 2. Predict Dropout Risk

```http
POST /ml/api/predict
Content-Type: application/json
```

**Request Body:**
```json
{
  "motivation_level": 7,
  "weekly_study_hours": 5.5,
  "free_time_hours_per_week": 10,
  "satisfaction_level": 6,
  "preferred_learning_mode": "online",
  "attendance_commitment": 8,
  "homework_completion_self": 7,
  "financial_stress_level": 3,
  "interaction_with_teacher": 6,
  "english_level_self": "B1",
  "goal_clarity_level": 8,
  "class_difficulty_level": 5,
  "peer_interaction_level": 7,
  "technical_issues_frequency": 2
}
```

**Response (200 OK):**
```json
{
  "dropout": "no",
  "probability": 0.85,
  "model": "KNN",
  "confidence": "High (85%)",
  "timestamp": "2026-05-04T10:30:05Z"
}
```

**Response (400 Bad Request):**
```json
{
  "detail": [
    {
      "loc": ["body", "motivation_level"],
      "msg": "ensure this value is less than or equal to 10",
      "type": "value_error.number.not_le"
    }
  ]
}
```

### 3. Interactive API Documentation

- **Swagger UI:** http://localhost:8090/docs
- **ReDoc:** http://localhost:8090/redoc

### Testing with cURL

```bash
# Test health endpoint
curl -X GET http://localhost:8090/ml/api/health

# Test prediction
curl -X POST http://localhost:8090/ml/api/predict \
  -H "Content-Type: application/json" \
  -d '{
    "motivation_level": 7,
    "weekly_study_hours": 5,
    "free_time_hours_per_week": 10,
    "satisfaction_level": 6,
    "preferred_learning_mode": "online",
    "attendance_commitment": 8,
    "homework_completion_self": 7,
    "financial_stress_level": 3,
    "interaction_with_teacher": 6,
    "english_level_self": "B1",
    "goal_clarity_level": 8,
    "class_difficulty_level": 5,
    "peer_interaction_level": 7,
    "technical_issues_frequency": 2
  }'
```

---

## Model Artifact Format

### Creating Your Model File

Export your trained KNN model as a single joblib file containing metadata and preprocessing artifacts.

### Required Fields

```python
artifacts = {
    "model": trained_estimator,                    # Required: fitted model
    "scaler": fitted_scaler,                       # Required: StandardScaler
    "encoders": {                                  # Required: dict of LabelEncoders
        "preferred_learning_mode": le_mode,
        "english_level_self": le_level,
    },
    "feature_order": feature_names,                # Required: list of feature names
    "label_mapping": {0: "no", 1: "yes"},         # Required: class labels
    "threshold": 0.5,                              # Optional: classification threshold
    "model_name": "KNN",                           # Optional: model identifier
}
```

### Export Script Example

```python
import joblib
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler, LabelEncoder

# Assuming you have trained model, scaler, and encoders
artifacts = {
    "model": best_model,                           # KNeighborsClassifier(n_neighbors=11)
    "scaler": scaler,                              # StandardScaler()
    "encoders": {
        "preferred_learning_mode": le_mode,       # LabelEncoder for categorical features
        "english_level_self": le_level,
    },
    "feature_order": [                             # Exact order of features at training time
        "motivation_level",
        "weekly_study_hours",
        "free_time_hours_per_week",
        "satisfaction_level",
        "preferred_learning_mode",
        "attendance_commitment",
        "homework_completion_self",
        "financial_stress_level",
        "interaction_with_teacher",
        "english_level_self",
        "goal_clarity_level",
        "class_difficulty_level",
        "peer_interaction_level",
        "technical_issues_frequency",
    ],
    "label_mapping": {0: "no", 1: "yes"},
    "threshold": 0.5,
    "model_name": "KNN",
}

# Export to joblib
joblib.dump(artifacts, "churn_prediction.joblib")
print("✅ Model exported to churn_prediction.joblib")
```

### Placement

Copy `churn_prediction.joblib` to:
```
ML_Dropout_Service/
├── models/
│   └── churn_prediction.joblib  ← Place your model here
├── app/
├── requirements.txt
└── README.md
```

---

## Troubleshooting

### Issue: `ModuleNotFoundError: No module named 'py_eureka_client'`

**Cause:** Dependencies not installed

**Solution:**
```bash
pip install -r requirements.txt
```

### Issue: `ERROR: Unknown compiler(s)` when installing numpy

**Cause:** Using Python 3.13 with old numpy version

**Solution:** Already fixed in updated `requirements.txt` - just run:
```bash
pip install -r requirements.txt
```

### Issue: Model file not found

**Error:** `FileNotFoundError: ./models/churn_prediction.joblib`

**Solution:**
1. Create `models/` folder: `mkdir models`
2. Place your model file: `cp /path/to/churn_prediction.joblib ./models/`
3. Verify: `ls models/` should show `churn_prediction.joblib`

### Issue: Eureka registration fails

**Error:** `ConnectionError: Unable to connect to Eureka server`

**Solution:**
1. Verify Eureka is running: `curl http://localhost:8761/eureka/apps`
2. Check `EUREKA_SERVER` environment variable
3. Run without Eureka (development):
   ```bash
   # Edit app/eureka_client.py and comment out init_eureka() call
   # Or set environment variable: EUREKA_ENABLED=false
   ```

### Issue: Port 8090 already in use

**Error:** `Address already in use`

**Solution:**
```bash
# Use different port
uvicorn app.main:app --host 0.0.0.0 --port 8091

# Or kill existing process
# Windows:
netstat -ano | findstr :8090
taskkill /PID <PID> /F

# macOS/Linux:
lsof -i :8090
kill -9 <PID>
```

### Issue: Slow prediction responses

**Cause:** Single worker process

**Solution:** Use multiple workers:
```bash
uvicorn app.main:app --workers 4 --port 8090
```

---

## Architecture

### Component Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway (8081)                       │
│                    (Spring Cloud Gateway)                   │
└──────────────────────────┬──────────────────────────────────┘
                           │
                    Route: /ml/api/**
                           │
                ┌──────────▼──────────┐
                │  ML Dropout Service  │
                │  (FastAPI, port 8090)│
                │  ┌────────────────┐ │
                │  │ /ml/api/health │ │
                │  ├────────────────┤ │
                │  │/ml/api/predict │ │
                │  └────────────────┘ │
                └──────────┬───────────┘
                           │
                ┌──────────▼──────────┐
                │  Eureka Registry    │
                │  (localhost:8761)   │
                └─────────────────────┘
```

### Request Flow

```
1. Client (Angular Frontend / Spring Service)
   ↓
2. API Gateway (/ml/api/predict)
   ↓
3. Eureka Lookup (ml-dropout-service)
   ↓
4. ML Service receives request
   ↓
5. Load model + scaler + encoders
   ↓
6. Preprocess input features
   ↓
7. Run KNN inference
   ↓
8. Generate prediction response
   ↓
9. Return to client
```

---

## Performance Considerations

| Factor | Recommendation |
|---|---|
| **Worker Processes** | 4-8 (based on CPU cores) |
| **Model Size** | Keep <500MB (should be ~10-50MB) |
| **Inference Time** | <100ms per request |
| **Concurrent Requests** | 100+ with proper configuration |
| **Memory Usage** | ~200-500MB per worker |

---

## Security Notes

⚠️ **For Production:**

1. **Disable Swagger UI:**
   ```python
   app = FastAPI(docs_url=None, redoc_url=None)
   ```

2. **Enable HTTPS:**
   ```bash
   uvicorn app.main:app --ssl-keyfile=key.pem --ssl-certfile=cert.pem
   ```

3. **Restrict CORS:**
   ```python
   from fastapi.middleware.cors import CORSMiddleware
   
   app.add_middleware(
       CORSMiddleware,
       allow_origins=["https://yourdomain.com"],
       allow_credentials=True,
   )
   ```

4. **Add Authentication:**
   - Implement JWT token validation
   - Use API keys for external requests

---

## Next Steps

1. ✅ Install dependencies
2. ✅ Place trained model in `./models/churn_prediction.joblib`
3. ✅ Start service: `uvicorn app.main:app --host 0.0.0.0 --port 8090`
4. ✅ Test endpoint: `curl http://localhost:8090/ml/api/health`
5. ✅ Verify Eureka registration
6. ✅ Test via API Gateway: `http://localhost:8081/ml/api/predict`

---

## Support & Links

- **FastAPI Docs:** https://fastapi.tiangolo.com/
- **Uvicorn Docs:** https://www.uvicorn.org/
- **Eureka Python Client:** https://github.com/keijack/python-eureka-client
- **scikit-learn KNN:** https://scikit-learn.org/stable/modules/generated/sklearn.neighbors.KNeighborsClassifier.html

---

**Version:** 1.0.0 | **Last Updated:** May 2026
