import joblib
import os

MODEL_PATH = r"e:\DevOps-PI\Esprit-PIDEV-4SAE7-2026-DEVUNITY\Back-PIDEV-4SAE7-2026-DEVUNITY\ML_Dropout_Service\models\resume.joblib"

if os.path.exists(MODEL_PATH):
    model = joblib.load(MODEL_PATH)
    print(f"Type of loaded model: {type(model)}")
    if isinstance(model, dict):
        print(f"Keys in dict: {model.keys()}")
    elif hasattr(model, 'named_steps'):
        print(f"Steps in pipeline: {model.named_steps.keys()}")
    else:
        print("Model is a single estimator.")
else:
    print("Model file not found.")
