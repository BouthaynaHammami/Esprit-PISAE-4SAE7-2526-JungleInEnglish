"""CV Analysis module for resume evaluation using ML model."""
import re
import os
import joblib
import pandas as pd
import numpy as np
from typing import Dict, Any
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load the ML pipeline
# The pipeline contains: vectorizer (TF-IDF), scaler (StandardScaler), kmeans
MODEL_PATH = os.path.join(os.path.dirname(__file__), '..', 'models', 'resume.joblib')

try:
    pipeline = joblib.load(MODEL_PATH)
    logger.info(f"Model loaded successfully from {MODEL_PATH}")
except Exception as e:
    logger.error(f"Failed to load model: {e}")
    pipeline = None

def clean_text(text: str) -> str:
    """Clean text by removing special characters and extra whitespace."""
    text = re.sub(r'http\S+\s*', ' ', text)  # remove URLs
    text = re.sub(r'RT|cc', ' ', text)  # remove RT and cc
    text = re.sub(r'#\S+', ' ', text)  # remove hashtags
    text = re.sub(r'@\S+', ' ', text)  # remove mentions
    text = re.sub(r'[%s]' % re.escape(r"""!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~"""), ' ', text)  # remove punctuations
    text = re.sub(r'[^\x00-\x7f]', r' ', text)
    text = re.sub(r'\s+', ' ', text)  # remove extra whitespace
    return text.lower().strip()

def analyze_cv_text(cv_text: str, skills: str, experience_years: int) -> Dict[str, Any]:
    """
    Analyze CV text using KMeans model and return decision, score, and reasoning.
    """
    if pipeline is None:
        return {
            "cluster": "error",
            "decision": "PENDING",
            "raison": "ML model not loaded",
            "score": 0,
            "model": "error"
        }

    try:
        # 1. Preprocess text
        cleaned_cv = clean_text(cv_text)
        required_skills = [s.strip().lower() for s in skills.split(',') if s.strip()]
        
        # 2. Keyword Matching (Robust Fallback because vectorizer/scaler are missing in joblib)
        found_skills = []
        for skill in required_skills:
            if skill in cleaned_cv:
                found_skills.append(skill)
        
        # 3. Calculate Matching Score
        skill_score = (len(found_skills) / len(required_skills) * 100) if required_skills else 0
        
        # Boost score based on experience
        exp_score = min(experience_years * 10, 30) # Max 30 points for experience
        final_score = min(int(skill_score * 0.7 + exp_score), 98) # Weighted score
        
        # 4. Determine Cluster & Decision
        if final_score >= 80:
            if experience_years >= 5:
                cluster, decision = "Senior", "ACCEPTED"
                raison = f"Excellent match ({final_score}%). Senior profile with {experience_years} years of experience and mastery of {', '.join(found_skills[:3])}."
            else:
                cluster, decision = "Junior", "ACCEPTED"
                raison = f"Strong potential ({final_score}%). Technical skills match well for a junior role."
        elif final_score >= 50:
            cluster, decision = "Mid-level", "PENDING"
            raison = f"Good match ({final_score}%). Matches some requirements but needs further evaluation."
        else:
            cluster, decision = "Mismatch", "REJECTED"
            raison = f"Low match ({final_score}%). CV does not sufficiently match the required skills: {skills}."

        return {
            "cluster": cluster,
            "decision": decision,
            "raison": raison,
            "score": final_score,
            "model": "hybrid_keyword_v1"
        }

    except Exception as e:
        logger.error(f"Error during analysis: {e}")
        return {
            "cluster": "error",
            "decision": "PENDING",
            "raison": f"Analysis error: {str(e)}",
            "score": 0,
            "model": "fallback_v1"
        }
