"""CV Analysis module for resume evaluation."""
import re
from typing import Dict, Any


def analyze_cv_text(cv_text: str, skills: str, experience_years: int) -> Dict[str, Any]:
    """
    Analyze CV text and return decision, score, and reasoning.
    
    Args:
        cv_text: The text content of the CV
        skills: Required skills for the position
        experience_years: Required years of experience
        
    Returns:
        Dictionary with cluster, decision, raison, score, and model
    """
    # Simple keyword-based analysis
    cv_lower = cv_text.lower()
    skills_lower = skills.lower()
    
    # Calculate score based on various factors
    score = 0
    reasons = []
    
    # Check for skills match (40 points)
    skill_keywords = skills_lower.split()
    matched_skills = sum(1 for skill in skill_keywords if skill in cv_lower)
    if skill_keywords:
        skill_score = (matched_skills / len(skill_keywords)) * 40
        score += skill_score
        if skill_score > 20:
            reasons.append(f"Good skills match ({matched_skills}/{len(skill_keywords)} keywords found)")
        else:
            reasons.append(f"Limited skills match ({matched_skills}/{len(skill_keywords)} keywords found)")
    
    # Check for experience indicators (30 points)
    experience_keywords = ['experience', 'years', 'worked', 'developed', 'managed', 'led', 'created']
    experience_count = sum(1 for keyword in experience_keywords if keyword in cv_lower)
    experience_score = min(experience_count * 5, 30)
    score += experience_score
    if experience_score > 15:
        reasons.append("Strong experience indicators found")
    else:
        reasons.append("Limited experience indicators")
    
    # Check for education (15 points)
    education_keywords = ['degree', 'bachelor', 'master', 'university', 'college', 'diploma', 'certification']
    education_count = sum(1 for keyword in education_keywords if keyword in cv_lower)
    education_score = min(education_count * 5, 15)
    score += education_score
    if education_score > 7:
        reasons.append("Good educational background")
    
    # Check for technical terms (15 points)
    technical_keywords = ['project', 'software', 'development', 'programming', 'design', 'implementation', 'testing']
    technical_count = sum(1 for keyword in technical_keywords if keyword in cv_lower)
    technical_score = min(technical_count * 3, 15)
    score += technical_score
    if technical_score > 7:
        reasons.append("Strong technical background")
    
    # Round score to integer
    score = int(round(score))
    
    # Determine decision based on score
    if score >= 70:
        decision = "ACCEPTED"
        cluster = "high_potential"
    elif score >= 50:
        decision = "PENDING"
        cluster = "medium_potential"
    else:
        decision = "REJECTED"
        cluster = "low_potential"
    
    # Build reasoning text
    raison = " | ".join(reasons) if reasons else "Basic CV analysis completed"
    
    return {
        "cluster": cluster,
        "decision": decision,
        "raison": raison,
        "score": score,
        "model": "cv_keyword_analyzer_v1"
    }
