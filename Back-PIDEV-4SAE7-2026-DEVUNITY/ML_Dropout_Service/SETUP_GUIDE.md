# 🚀 ML Dropout Service - Complete Setup & Usage Guide

**Version:** 1.0.0  
**Updated:** May 4, 2026  
**Status:** ✅ Production Ready

---

## 📌 Quick Reference

| Component | Details |
|---|---|
| **Language** | Python 3.13 |
| **Framework** | FastAPI 0.111.0 |
| **Server** | Uvicorn 0.30.1 |
| **ML Model** | scikit-learn KNeighborsClassifier |
| **Port** | 8090 |
| **Service Name** | ml-dropout-service |

---

## ⚡ 60-Second Quick Start

```powershell
# Navigate to service directory
cd ML_Dropout_Service

# Install dependencies (one-time)
pip install -r requirements.txt --only-binary :all:

# Copy your trained model
copy \path\to\churn_prediction.joblib .\models\

# Start the service
uvicorn app.main:app --host 0.0.0.0 --port 8090
```

Visit: **http://localhost:8090/docs** ← Interactive API documentation

---

## 📋 Prerequisites

### System Requirements

- **OS:** Windows 10/11, macOS, Linux
- **Python:** 3.13 (⚠️ Must be 3.13+ for numpy 2.x support)
- **RAM:** 1GB minimum (2GB recommended)
- **Disk:** 500MB free space
- **Network:** For Eureka registration (optional for development)

### Check Your Setup

```powershell
# Check Python version
python --version
# Expected: Python 3.13.x

# Check Python location
python -c "import sys; print(sys.executable)"
```

---

## 🔧 Installation Steps

### Step 1️⃣: Navigate to Service Directory

```powershell
cd Back-PIDEV-4SAE7-2026-DEVUNITY\ML_Dropout_Service
```

**Verify you're in the right directory:**
```powershell
ls
# You should see: app/, models/, requirements.txt, README.md, Dockerfile
```

### Step 2️⃣: Install Dependencies

**First time (recommended):**
```powershell
pip install -r requirements.txt --only-binary :all:
```

**Why `--only-binary`?**  
Prevents source compilation errors and ensures compatibility with Python 3.13.

**Alternative (with virtual environment - recommended for production):**
```powershell
# Create virtual environment
python -m venv venv

# Activate it
.\venv\Scripts\Activate.ps1

# Install
pip install -r requirements.txt --only-binary :all:
```

**Verify Installation:**
```powershell
python -c "import fastapi, pydantic, joblib, numpy, pandas, sklearn; print('✅ All imports OK')"
```

### Step 3️⃣: Prepare Your Model

**Create models directory:**
```powershell
mkdir models -Force
```

**Place your trained model:**
```powershell
# Copy from wherever you trained it
copy C:\path\to\your\churn_prediction.joblib .\models\
```

**Verify model is loaded:**
```powershell
python -c "import joblib; m = joblib.load('./models/churn_prediction.joblib'); print(f'✅ Model loaded: {m.get(\"model_name\", \"Unknown\")}')"
```

### Step 4️⃣: Configure Environment (Optional)

Create `.env` file:
```powershell
# Create .env file
@"
EUREKA_SERVER=http://localhost:8761/eureka
SERVICE_NAME=ml-dropout-service
SERVICE_PORT=8090
LOG_LEVEL=INFO
MODEL_PATH=./models/churn_prediction.joblib
"@ | Out-File -FilePath .env -Encoding UTF8
```

---

## 🎯 Running the Service

### Development Mode (with auto-reload)

```powershell
uvicorn app.main:app --reload --host 0.0.0.0 --port 8090
```

**Output indicates success:**
```
INFO:     Uvicorn running on http://0.0.0.0:8090
INFO:     Application startup complete
```

### Production Mode (4 workers for concurrency)

```powershell
uvicorn app.main:app --host 0.0.0.0 --port 8090 --workers 4
```

### Custom Port

```powershell
# Use port 8091 instead
uvicorn app.main:app --host 0.0.0.0 --port 8091
```

### Stop the Service

```
Press Ctrl+C in the terminal
```

---

## 🧪 Testing the Service

### Test 1: Health Check

```powershell
# Using PowerShell
(Invoke-WebRequest http://localhost:8090/ml/api/health).Content | ConvertFrom-Json

# Or using curl
curl http://localhost:8090/ml/api/health
```

**Expected Response:**
```json
{
  "status": "ok",
  "model_loaded": true,
  "service_version": "1.0.0",
  "timestamp": "2026-05-04T10:30:00Z"
}
```

### Test 2: Prediction Request

```powershell
# Using PowerShell
$body = @{
    motivation_level = 7
    weekly_study_hours = 5.5
    free_time_hours_per_week = 10
    satisfaction_level = 6
    preferred_learning_mode = "online"
    attendance_commitment = 8
    homework_completion_self = 7
    financial_stress_level = 3
    interaction_with_teacher = 6
    english_level_self = "B1"
    goal_clarity_level = 8
    class_difficulty_level = 5
    peer_interaction_level = 7
    technical_issues_frequency = 2
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8090/ml/api/predict `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

**Expected Response:**
```json
{
  "dropout": "no",
  "probability": 0.85,
  "model": "KNN",
  "confidence": "High (85%)"
}
```

### Test 3: Interactive API Docs

```
http://localhost:8090/docs     ← Swagger UI
http://localhost:8090/redoc    ← ReDoc Documentation
```

Try predictions directly in the browser!

---

## 🔌 Eureka Service Registration

### Enable Eureka Registration

Service automatically registers on startup. To verify:

```powershell
# Check if service is registered
curl http://localhost:8761/eureka/apps

# You should see ml-dropout-service in the response
```

### For Development (Disable Eureka)

Edit `app/eureka_client.py`:
```python
# Comment out this line
# init_eureka()
```

Or set environment variable:
```powershell
$env:EUREKA_ENABLED = "false"
```

---

## 📊 API Reference

### Endpoints

#### 1. Health Check
```http
GET /ml/api/health
```

**Response:** `200 OK`
```json
{
  "status": "ok",
  "model_loaded": true,
  "service_version": "1.0.0",
  "timestamp": "ISO 8601 timestamp"
}
```

#### 2. Predict Dropout Risk
```http
POST /ml/api/predict
Content-Type: application/json
```

**Request Body (14 fields required):**
```json
{
  "motivation_level": 1-10,
  "weekly_study_hours": number,
  "free_time_hours_per_week": number,
  "satisfaction_level": 1-10,
  "preferred_learning_mode": "online|in-person|hybrid",
  "attendance_commitment": 1-10,
  "homework_completion_self": 1-10,
  "financial_stress_level": 1-10,
  "interaction_with_teacher": 1-10,
  "english_level_self": "A1|A2|B1|B2|C1|C2",
  "goal_clarity_level": 1-10,
  "class_difficulty_level": 1-10,
  "peer_interaction_level": 1-10,
  "technical_issues_frequency": 1-10
}
```

**Response:** `200 OK`
```json
{
  "dropout": "yes|no",
  "probability": 0.0-1.0,
  "model": "KNN",
  "confidence": "High|Medium|Low (XX%)"
}
```

**Error Response:** `400 Bad Request`
```json
{
  "detail": "Validation error details..."
}
```

---

## 🛠️ Troubleshooting

### Issue: "ModuleNotFoundError: No module named 'fastapi'"

**Cause:** Dependencies not installed

**Solution:**
```powershell
pip install -r requirements.txt --only-binary :all:
```

### Issue: "FileNotFoundError: ./models/churn_prediction.joblib"

**Cause:** Model file not found

**Solution:**
```powershell
# 1. Create models directory
mkdir models -Force

# 2. Copy your model file
copy C:\path\to\model .\models\churn_prediction.joblib

# 3. Verify
ls models/
```

### Issue: "Address already in use"

**Cause:** Port 8090 is occupied

**Solution:**
```powershell
# Option 1: Use different port
uvicorn app.main:app --port 8091

# Option 2: Kill process on port 8090
netstat -ano | findstr :8090
taskkill /PID <PID> /F
```

### Issue: Slow inference time

**Cause:** Single worker process

**Solution:**
```powershell
uvicorn app.main:app --workers 4 --port 8090
```

### Issue: "Eureka connection refused"

**Cause:** Eureka server not running (only for service mesh setup)

**Solution:**
- Either start Eureka: `docker run -p 8761:8761 eureka-server`
- Or disable Eureka for development (see above)

---

## 📦 Dependencies Explained

| Package | Version | Purpose |
|---|---|---|
| `fastapi` | 0.111.0 | Web framework |
| `uvicorn` | 0.30.1 | ASGI server |
| `pydantic` | 2.7.1 | Data validation |
| `joblib` | 1.4.2 | Model serialization |
| `numpy` | ≥2.0.0 | Numerical computing |
| `pandas` | ≥2.1.0 | Data manipulation |
| `scikit-learn` | ≥1.3.0 | ML algorithms |
| `py-eureka-client` | 0.11.7 | Service discovery |

---

## 📝 Model Format

Your model file must contain these artifacts in joblib format:

```python
artifacts = {
    "model": trained_knn_estimator,                # Required
    "scaler": StandardScaler_instance,              # Required
    "encoders": {                                   # Required
        "preferred_learning_mode": LabelEncoder(),
        "english_level_self": LabelEncoder(),
    },
    "feature_order": [                              # Required
        "motivation_level",
        "weekly_study_hours",
        # ... 12 more features
    ],
    "label_mapping": {0: "no", 1: "yes"},          # Required
    "threshold": 0.5,                               # Optional
    "model_name": "KNN",                            # Optional
}

joblib.dump(artifacts, "churn_prediction.joblib")
```

---

## 🔒 Security Checklist

- [ ] Restrict CORS in production
- [ ] Disable Swagger UI in production  
- [ ] Enable HTTPS/TLS
- [ ] Implement authentication (JWT tokens)
- [ ] Use environment variables for secrets
- [ ] Rate limit requests
- [ ] Log all predictions
- [ ] Monitor resource usage

---

## 📊 Performance Tuning

### For High Throughput

```powershell
# Use more workers (based on CPU cores)
uvicorn app.main:app --workers 8 --port 8090 --loop uvloop
```

### For Low Latency

```powershell
# Single worker with fast event loop
uvicorn app.main:app --workers 1 --port 8090 --loop uvloop
```

### Memory Optimization

- Keep model size <500MB
- Use model quantization if possible
- Monitor with: `tasklist /FI "ImageName eq python.exe"`

---

## 📚 Additional Resources

- **FastAPI Documentation:** https://fastapi.tiangolo.com/
- **Uvicorn Documentation:** https://www.uvicorn.org/
- **scikit-learn KNN:** https://scikit-learn.org/stable/modules/neighbors.html
- **Pydantic Validation:** https://docs.pydantic.dev/

---

## 🆘 Support

If you encounter issues:

1. ✅ Check Python version: `python --version`
2. ✅ Verify dependencies: `pip list | findstr fastapi`
3. ✅ Test health endpoint: `curl http://localhost:8090/ml/api/health`
4. ✅ Check logs for detailed errors
5. ✅ Review the README.md for detailed API docs

---

## ✅ Deployment Checklist

- [ ] Dependencies installed with `--only-binary :all:`
- [ ] Model file placed in `./models/churn_prediction.joblib`
- [ ] Model format verified (contains all required artifacts)
- [ ] Health endpoint responds successfully
- [ ] Prediction endpoint returns valid responses
- [ ] Eureka registration confirmed (if using service mesh)
- [ ] Environment variables configured (.env file)
- [ ] Security headers configured
- [ ] Logging enabled for monitoring
- [ ] Load testing completed (100+ requests/sec)

---

**Last Updated:** May 4, 2026  
**Maintained By:** AI Assistant  
**License:** MIT
