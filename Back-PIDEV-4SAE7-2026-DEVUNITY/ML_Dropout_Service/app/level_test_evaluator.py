"""Level Test Evaluator for oral and paragraph tests."""
import re
from typing import Dict, Any


def evaluate_paragraph_test(paragraph_text: str, subject_title: str) -> Dict[str, Any]:
    """
    Evaluate a paragraph writing test.
    
    Args:
        paragraph_text: The paragraph written by the student
        subject_title: The subject/topic of the test
        
    Returns:
        Dictionary with level, score, and feedback
    """
    if not paragraph_text or len(paragraph_text.strip()) < 10:
        return {
            "level": "A1",
            "score": 0,
            "feedback": "Paragraph is too short or empty",
            "details": {
                "word_count": 0,
                "sentence_count": 0,
                "complexity_score": 0
            }
        }
    
    # Calculate metrics
    words = paragraph_text.split()
    word_count = len(words)
    sentences = re.split(r'[.!?]+', paragraph_text)
    sentence_count = len([s for s in sentences if s.strip()])
    
    # Calculate average word length (complexity indicator)
    avg_word_length = sum(len(word) for word in words) / word_count if word_count > 0 else 0
    
    # Calculate score based on metrics
    score = 0
    feedback_parts = []
    
    # Word count scoring (40 points)
    if word_count >= 150:
        score += 40
        feedback_parts.append("Excellent length")
    elif word_count >= 100:
        score += 30
        feedback_parts.append("Good length")
    elif word_count >= 50:
        score += 20
        feedback_parts.append("Adequate length")
    else:
        score += 10
        feedback_parts.append("Too short")
    
    # Sentence structure (30 points)
    if sentence_count >= 8:
        score += 30
        feedback_parts.append("Well-structured")
    elif sentence_count >= 5:
        score += 20
        feedback_parts.append("Good structure")
    else:
        score += 10
        feedback_parts.append("Simple structure")
    
    # Vocabulary complexity (30 points)
    if avg_word_length >= 6:
        score += 30
        feedback_parts.append("Advanced vocabulary")
    elif avg_word_length >= 5:
        score += 20
        feedback_parts.append("Good vocabulary")
    else:
        score += 10
        feedback_parts.append("Basic vocabulary")
    
    # Determine CEFR level based on score
    if score >= 85:
        level = "C2"
    elif score >= 75:
        level = "C1"
    elif score >= 65:
        level = "B2"
    elif score >= 55:
        level = "B1"
    elif score >= 40:
        level = "A2"
    else:
        level = "A1"
    
    return {
        "level": level,
        "score": score,
        "feedback": " | ".join(feedback_parts),
        "details": {
            "word_count": word_count,
            "sentence_count": sentence_count,
            "avg_word_length": round(avg_word_length, 2),
            "complexity_score": score
        }
    }


def evaluate_oral_test(transcript: str, duration_seconds: int = 60) -> Dict[str, Any]:
    """
    Evaluate an oral test based on transcript.
    
    Args:
        transcript: The transcribed speech
        duration_seconds: Duration of the oral test in seconds
        
    Returns:
        Dictionary with level, score, and feedback
    """
    if not transcript or len(transcript.strip()) < 10:
        return {
            "level": "A1",
            "score": 0,
            "feedback": "Transcript is too short or empty",
            "details": {
                "word_count": 0,
                "words_per_minute": 0,
                "fluency_score": 0
            }
        }
    
    # Calculate metrics
    words = transcript.split()
    word_count = len(words)
    words_per_minute = (word_count / duration_seconds) * 60 if duration_seconds > 0 else 0
    
    # Calculate score
    score = 0
    feedback_parts = []
    
    # Fluency - words per minute (40 points)
    if words_per_minute >= 150:
        score += 40
        feedback_parts.append("Excellent fluency")
    elif words_per_minute >= 120:
        score += 30
        feedback_parts.append("Good fluency")
    elif words_per_minute >= 90:
        score += 20
        feedback_parts.append("Moderate fluency")
    else:
        score += 10
        feedback_parts.append("Limited fluency")
    
    # Vocabulary range (30 points)
    unique_words = len(set(word.lower() for word in words))
    vocabulary_ratio = unique_words / word_count if word_count > 0 else 0
    
    if vocabulary_ratio >= 0.7:
        score += 30
        feedback_parts.append("Rich vocabulary")
    elif vocabulary_ratio >= 0.5:
        score += 20
        feedback_parts.append("Good vocabulary range")
    else:
        score += 10
        feedback_parts.append("Limited vocabulary")
    
    # Length and completeness (30 points)
    if word_count >= 150:
        score += 30
        feedback_parts.append("Complete response")
    elif word_count >= 100:
        score += 20
        feedback_parts.append("Adequate response")
    else:
        score += 10
        feedback_parts.append("Brief response")
    
    # Determine CEFR level
    if score >= 85:
        level = "C2"
    elif score >= 75:
        level = "C1"
    elif score >= 65:
        level = "B2"
    elif score >= 55:
        level = "B1"
    elif score >= 40:
        level = "A2"
    else:
        level = "A1"
    
    return {
        "level": level,
        "score": score,
        "feedback": " | ".join(feedback_parts),
        "details": {
            "word_count": word_count,
            "unique_words": unique_words,
            "words_per_minute": round(words_per_minute, 1),
            "vocabulary_ratio": round(vocabulary_ratio, 2)
        }
    }
