"""Course Recommendation System based on student level."""
from typing import Dict, List, Any


# CEFR Level to Course Mapping
LEVEL_COURSE_RECOMMENDATIONS = {
    "A1": {
        "level_name": "Beginner",
        "recommended_courses": [
            {
                "title": "English Basics for Beginners",
                "description": "Learn fundamental English grammar, basic vocabulary, and simple sentence structures",
                "topics": ["Alphabet & Pronunciation", "Basic Greetings", "Numbers & Colors", "Simple Present Tense", "Common Verbs"],
                "duration": "4 weeks",
                "difficulty": "Beginner"
            },
            {
                "title": "Everyday English Conversations",
                "description": "Practice basic conversations for daily situations",
                "topics": ["Introducing Yourself", "Shopping", "Asking for Directions", "Ordering Food", "Basic Questions"],
                "duration": "3 weeks",
                "difficulty": "Beginner"
            },
            {
                "title": "English Vocabulary Builder A1",
                "description": "Build your essential English vocabulary with 500+ common words",
                "topics": ["Family & Friends", "Food & Drinks", "Home & Furniture", "Weather", "Time & Dates"],
                "duration": "4 weeks",
                "difficulty": "Beginner"
            }
        ],
        "skills_to_improve": [
            "Basic grammar structures",
            "Essential vocabulary (500-1000 words)",
            "Simple sentence formation",
            "Basic listening comprehension",
            "Elementary reading skills"
        ]
    },
    "A2": {
        "level_name": "Elementary",
        "recommended_courses": [
            {
                "title": "Elementary English Grammar",
                "description": "Master elementary grammar including past tense, future tense, and basic modals",
                "topics": ["Past Simple", "Future with 'will'", "Present Continuous", "Modal Verbs", "Comparatives"],
                "duration": "5 weeks",
                "difficulty": "Elementary"
            },
            {
                "title": "Practical English for Travel",
                "description": "Learn English for traveling, hotels, airports, and tourist situations",
                "topics": ["At the Airport", "Hotel Check-in", "Public Transportation", "Emergency Situations", "Tourist Attractions"],
                "duration": "4 weeks",
                "difficulty": "Elementary"
            },
            {
                "title": "English Reading & Writing A2",
                "description": "Improve your reading comprehension and basic writing skills",
                "topics": ["Short Stories", "Email Writing", "Simple Essays", "Reading Strategies", "Paragraph Structure"],
                "duration": "6 weeks",
                "difficulty": "Elementary"
            }
        ],
        "skills_to_improve": [
            "Past and future tenses",
            "Expanded vocabulary (1000-2000 words)",
            "Basic writing skills",
            "Improved listening comprehension",
            "Simple conversation skills"
        ]
    },
    "B1": {
        "level_name": "Intermediate",
        "recommended_courses": [
            {
                "title": "Intermediate English Grammar",
                "description": "Advanced grammar including perfect tenses, conditionals, and passive voice",
                "topics": ["Present Perfect", "Past Perfect", "Conditionals (1st & 2nd)", "Passive Voice", "Reported Speech"],
                "duration": "6 weeks",
                "difficulty": "Intermediate"
            },
            {
                "title": "Business English Fundamentals",
                "description": "Learn English for professional and business contexts",
                "topics": ["Business Emails", "Meetings & Presentations", "Negotiations", "Phone Calls", "Professional Writing"],
                "duration": "5 weeks",
                "difficulty": "Intermediate"
            },
            {
                "title": "English Conversation & Fluency B1",
                "description": "Develop fluency through discussions, debates, and extended conversations",
                "topics": ["Expressing Opinions", "Debates", "Storytelling", "Idioms & Phrases", "Cultural Topics"],
                "duration": "5 weeks",
                "difficulty": "Intermediate"
            }
        ],
        "skills_to_improve": [
            "Complex grammar structures",
            "Professional vocabulary (2000-3000 words)",
            "Fluent conversation skills",
            "Academic writing basics",
            "Listening to native speakers"
        ]
    },
    "B2": {
        "level_name": "Upper Intermediate",
        "recommended_courses": [
            {
                "title": "Advanced English Grammar",
                "description": "Master advanced grammar including subjunctive, inversion, and complex structures",
                "topics": ["Subjunctive Mood", "Inversion", "Cleft Sentences", "Advanced Conditionals", "Participle Clauses"],
                "duration": "6 weeks",
                "difficulty": "Upper Intermediate"
            },
            {
                "title": "Academic English Writing",
                "description": "Develop academic writing skills for essays, reports, and research papers",
                "topics": ["Essay Structure", "Research Papers", "Citations", "Critical Analysis", "Academic Vocabulary"],
                "duration": "7 weeks",
                "difficulty": "Upper Intermediate"
            },
            {
                "title": "English for Professional Communication",
                "description": "Advanced business English for presentations, negotiations, and leadership",
                "topics": ["Executive Presentations", "Strategic Negotiations", "Leadership Communication", "Report Writing", "Networking"],
                "duration": "6 weeks",
                "difficulty": "Upper Intermediate"
            }
        ],
        "skills_to_improve": [
            "Advanced grammar mastery",
            "Extensive vocabulary (3000-4000 words)",
            "Academic writing proficiency",
            "Professional presentation skills",
            "Understanding complex texts"
        ]
    },
    "C1": {
        "level_name": "Advanced",
        "recommended_courses": [
            {
                "title": "Advanced English Literature",
                "description": "Analyze and discuss classic and contemporary English literature",
                "topics": ["Literary Analysis", "Poetry", "Drama", "Novels", "Critical Theory"],
                "duration": "8 weeks",
                "difficulty": "Advanced"
            },
            {
                "title": "Professional English Mastery",
                "description": "Master professional English for executive-level communication",
                "topics": ["Executive Communication", "Strategic Planning", "Crisis Management", "International Business", "Cross-cultural Communication"],
                "duration": "7 weeks",
                "difficulty": "Advanced"
            },
            {
                "title": "Advanced Academic Writing & Research",
                "description": "Develop skills for academic research, thesis writing, and publication",
                "topics": ["Research Methodology", "Thesis Writing", "Academic Publishing", "Literature Review", "Data Presentation"],
                "duration": "8 weeks",
                "difficulty": "Advanced"
            }
        ],
        "skills_to_improve": [
            "Near-native fluency",
            "Sophisticated vocabulary (4000-5000 words)",
            "Complex academic writing",
            "Nuanced expression",
            "Cultural and idiomatic mastery"
        ]
    },
    "C2": {
        "level_name": "Mastery",
        "recommended_courses": [
            {
                "title": "English Language Teaching Certification",
                "description": "Prepare to teach English as a second language",
                "topics": ["Teaching Methodology", "Lesson Planning", "Assessment", "Classroom Management", "TESOL Certification"],
                "duration": "10 weeks",
                "difficulty": "Mastery"
            },
            {
                "title": "Advanced Translation & Interpretation",
                "description": "Develop professional translation and interpretation skills",
                "topics": ["Translation Techniques", "Simultaneous Interpretation", "Consecutive Interpretation", "Specialized Translation", "Ethics"],
                "duration": "8 weeks",
                "difficulty": "Mastery"
            },
            {
                "title": "English Linguistics & Phonetics",
                "description": "Deep dive into English linguistics, phonetics, and language structure",
                "topics": ["Phonetics & Phonology", "Syntax", "Semantics", "Pragmatics", "Sociolinguistics"],
                "duration": "9 weeks",
                "difficulty": "Mastery"
            }
        ],
        "skills_to_improve": [
            "Native-level proficiency",
            "Specialized vocabulary (5000+ words)",
            "Professional expertise",
            "Teaching capabilities",
            "Linguistic analysis"
        ]
    }
}


def recommend_courses(level: str, score: int) -> Dict[str, Any]:
    """
    Recommend courses based on student's CEFR level and score.
    
    Args:
        level: CEFR level (A1, A2, B1, B2, C1, C2)
        score: Test score (0-100)
        
    Returns:
        Dictionary with course recommendations and learning path
    """
    if level not in LEVEL_COURSE_RECOMMENDATIONS:
        level = "A1"  # Default to beginner
    
    recommendations = LEVEL_COURSE_RECOMMENDATIONS[level]
    
    # Determine if student should also consider next level
    next_level_suggestion = None
    if score >= 85 and level != "C2":
        # Student is excelling, suggest next level courses too
        level_order = ["A1", "A2", "B1", "B2", "C1", "C2"]
        current_index = level_order.index(level)
        if current_index < len(level_order) - 1:
            next_level = level_order[current_index + 1]
            next_level_suggestion = {
                "level": next_level,
                "level_name": LEVEL_COURSE_RECOMMENDATIONS[next_level]["level_name"],
                "message": f"You're performing excellently! Consider challenging yourself with {next_level} level courses.",
                "preview_courses": LEVEL_COURSE_RECOMMENDATIONS[next_level]["recommended_courses"][:2]
            }
    
    return {
        "current_level": level,
        "level_name": recommendations["level_name"],
        "score": score,
        "recommended_courses": recommendations["recommended_courses"],
        "skills_to_improve": recommendations["skills_to_improve"],
        "next_level_suggestion": next_level_suggestion,
        "learning_path": {
            "current_focus": f"Master {level} ({recommendations['level_name']}) level skills",
            "estimated_time": "3-6 months with consistent practice",
            "study_tips": [
                "Practice daily for at least 30 minutes",
                "Engage in conversations with native speakers",
                "Read materials at your level regularly",
                "Watch English content with subtitles",
                "Complete all recommended courses in sequence"
            ]
        }
    }
