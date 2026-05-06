"""ML Models loader and predictor using actual joblib models."""
import os
import joblib
import numpy as np
from typing import Dict, Any, Optional
import logging

logger = logging.getLogger("ml_service")

# Global model storage
_models = {
    "classification": None,
    "regression": None,
    "resume": None
}


def load_all_models():
    """Load all ML models at startup."""
    global _models
    
    models_dir = "models"
    
    # Load classification model
    classification_path = os.path.join(models_dir, "best_classification_model.joblib")
    if os.path.exists(classification_path):
        try:
            _models["classification"] = joblib.load(classification_path)
            logger.info(f"Loaded classification model: {type(_models['classification'])}")
        except Exception as e:
            logger.error(f"Failed to load classification model: {e}")
    
    # Load regression model
    regression_path = os.path.join(models_dir, "best_regression_model.joblib")
    if os.path.exists(regression_path):
        try:
            _models["regression"] = joblib.load(regression_path)
            logger.info(f"Loaded regression model: {type(_models['regression'])}")
        except Exception as e:
            logger.error(f"Failed to load regression model: {e}")
    
    # Load resume clustering model
    resume_path = os.path.join(models_dir, "resume.joblib")
    if os.path.exists(resume_path):
        try:
            _models["resume"] = joblib.load(resume_path)
            logger.info(f"Loaded resume model: {type(_models['resume'])}")
        except Exception as e:
            logger.error(f"Failed to load resume model: {e}")


def get_model(model_name: str):
    """Get a loaded model by name."""
    return _models.get(model_name)


def predict_cv_classification(cv_features: np.ndarray) -> Dict[str, Any]:
    """
    Use classification model to predict CV acceptance.
    
    Args:
        cv_features: Feature vector for CV
        
    Returns:
        Dictionary with prediction results
    """
    model = get_model("classification")
    if model is None:
        raise ValueError("Classification model not loaded")
    
    try:
        # Make prediction
        prediction = model.predict(cv_features)
        
        # Get probability if available
        probability = None
        if hasattr(model, "predict_proba"):
            proba = model.predict_proba(cv_features)
            probability = float(proba[0][1])  # Probability of positive class
        
        # Map prediction to decision
        decision_map = {0: "REJECTED", 1: "ACCEPTED"}
        decision = decision_map.get(int(prediction[0]), "PENDING")
        
        # Calculate score based on probability or prediction
        if probability is not None:
            score = int(probability * 100)
        else:
            score = 100 if decision == "ACCEPTED" else 0
        
        return {
            "prediction": int(prediction[0]),
            "decision": decision,
            "probability": probability,
            "score": score
        }
    except Exception as e:
        logger.error(f"Prediction error: {e}")
        raise


def predict_level_regression(test_features: np.ndarray) -> Dict[str, Any]:
    """
    Use regression model to predict student level.
    
    Args:
        test_features: Feature vector for test performance
        
    Returns:
        Dictionary with level prediction
    """
    model = get_model("regression")
    if model is None:
        raise ValueError("Regression model not loaded")
    
    try:
        # Make prediction
        prediction = model.predict(test_features)
        predicted_score = float(prediction[0])
        
        # Map score to CEFR level
        if predicted_score >= 85:
            level = "C2"
        elif predicted_score >= 75:
            level = "C1"
        elif predicted_score >= 65:
            level = "B2"
        elif predicted_score >= 55:
            level = "B1"
        elif predicted_score >= 40:
            level = "A2"
        else:
            level = "A1"
        
        return {
            "predicted_score": predicted_score,
            "level": level,
            "score": int(predicted_score)
        }
    except Exception as e:
        logger.error(f"Regression prediction error: {e}")
        raise


def cluster_resume(cv_features: np.ndarray) -> Dict[str, Any]:
    """
    Use KMeans clustering model to cluster resumes.
    
    Args:
        cv_features: Feature vector for CV
        
    Returns:
        Dictionary with cluster assignment
    """
    model = get_model("resume")
    if model is None:
        raise ValueError("Resume clustering model not loaded")
    
    try:
        # Predict cluster
        cluster = model.predict(cv_features)
        cluster_id = int(cluster[0])
        
        # Map cluster to quality level
        cluster_names = {
            0: "low_potential",
            1: "medium_potential",
            2: "high_potential"
        }
        
        cluster_name = cluster_names.get(cluster_id, f"cluster_{cluster_id}")
        
        # Get distance to cluster center if available
        distance = None
        if hasattr(model, "transform"):
            distances = model.transform(cv_features)
            distance = float(distances[0][cluster_id])
        
        return {
            "cluster": cluster_id,
            "cluster_name": cluster_name,
            "distance_to_center": distance
        }
    except Exception as e:
        logger.error(f"Clustering error: {e}")
        raise


def extract_cv_features(cv_text: str, skills: str, experience_years: int) -> np.ndarray:
    """
    Extract features from CV text for model prediction.
    Simple feature extraction based on text analysis.
    
    Args:
        cv_text: CV text content
        skills: Required skills
        experience_years: Years of experience
        
    Returns:
        Feature vector as numpy array
    """
    # Simple feature extraction
    cv_lower = cv_text.lower()
    skills_lower = skills.lower()
    
    # Feature 1: Skills match ratio
    skill_keywords = skills_lower.split()
    matched_skills = sum(1 for skill in skill_keywords if skill in cv_lower)
    skills_ratio = matched_skills / len(skill_keywords) if skill_keywords else 0
    
    # Feature 2: Experience indicators
    experience_keywords = ['experience', 'years', 'worked', 'developed', 'managed']
    experience_count = sum(1 for keyword in experience_keywords if keyword in cv_lower)
    
    # Feature 3: Education indicators
    education_keywords = ['degree', 'bachelor', 'master', 'university', 'phd']
    education_count = sum(1 for keyword in education_keywords if keyword in cv_lower)
    
    # Feature 4: Technical terms
    technical_keywords = ['project', 'software', 'development', 'programming', 'design']
    technical_count = sum(1 for keyword in technical_keywords if keyword in cv_lower)
    
    # Feature 5: Word count
    word_count = len(cv_text.split())
    
    # Feature 6: Experience years (normalized)
    exp_normalized = min(experience_years / 10.0, 1.0)
    
    # Create feature vector (6 features)
    features = np.array([[
        skills_ratio,
        experience_count / 5.0,  # Normalize
        education_count / 5.0,   # Normalize
        technical_count / 5.0,   # Normalize
        min(word_count / 500.0, 1.0),  # Normalize
        exp_normalized
    ]])
    
    return features


def extract_test_features(paragraph_text: str, subject_title: str) -> np.ndarray:
    """
    Extract features from test paragraph for level prediction.
    
    Args:
        paragraph_text: Student's written paragraph
        subject_title: Test subject
        
    Returns:
        Feature vector as numpy array
    """
    words = paragraph_text.split()
    word_count = len(words)
    
    # Feature 1: Word count (normalized)
    word_count_norm = min(word_count / 200.0, 1.0)
    
    # Feature 2: Average word length
    avg_word_length = sum(len(word) for word in words) / word_count if word_count > 0 else 0
    avg_word_length_norm = min(avg_word_length / 10.0, 1.0)
    
    # Feature 3: Sentence count
    sentences = [s for s in paragraph_text.split('.') if s.strip()]
    sentence_count = len(sentences)
    sentence_count_norm = min(sentence_count / 10.0, 1.0)
    
    # Feature 4: Unique words ratio
    unique_words = len(set(word.lower() for word in words))
    unique_ratio = unique_words / word_count if word_count > 0 else 0
    
    # Feature 5: Complex words (>6 letters)
    complex_words = sum(1 for word in words if len(word) > 6)
    complex_ratio = complex_words / word_count if word_count > 0 else 0
    
    # Create feature vector (5 features)
    features = np.array([[
        word_count_norm,
        avg_word_length_norm,
        sentence_count_norm,
        unique_ratio,
        complex_ratio
    ]])
    
    return features
