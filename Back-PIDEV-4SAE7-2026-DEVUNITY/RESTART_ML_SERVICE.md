# How to Restart ML Service

## What I Added
✅ Created `ML_Dropout_Service/app/cv_analysis.py` - CV analysis logic
✅ Updated `ML_Dropout_Service/app/main.py` - Added `/ml/api/analyze-cv` endpoint
✅ Updated Spring Boot config - Changed endpoint to `/ml/api/analyze-cv`

## To Restart ML Service

### Option 1: Kill and Restart
```bash
# Find the Python process
Get-Process python

# Kill the ML service process (find the right PID)
Stop-Process -Id <PID> -Force

# Restart ML service
cd ML_Dropout_Service
python -m uvicorn app.main:app --host 0.0.0.0 --port 8090
```

### Option 2: If using systemd/service
```bash
sudo systemctl restart ml-service
```

### Option 3: If using Docker
```bash
docker restart ml-service
```

## Verify It's Working
```bash
# Test the new endpoint
curl -X POST http://localhost:8090/ml/api/analyze-cv \
  -H "Content-Type: application/json" \
  -d '{"cvText":"Java developer with 5 years experience","skills":"Java Spring","experienceYears":3}'
```

Expected response:
```json
{
  "cluster": "high_potential",
  "decision": "ACCEPTED",
  "raison": "Good skills match (2/2 keywords found) | Strong experience indicators found | Good educational background",
  "score": 75,
  "model": "cv_keyword_analyzer_v1"
}
```

## Then Test from Spring Boot
Once ML service is restarted, test the full integration:
```bash
curl -X POST "http://localhost:8083/activities/api/api/applicants/2/analyze?recruitmentId=2"
```
