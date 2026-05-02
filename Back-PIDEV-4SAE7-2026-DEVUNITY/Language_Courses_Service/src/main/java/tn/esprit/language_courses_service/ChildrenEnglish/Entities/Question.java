package tn.esprit.language_courses_service.ChildrenEnglish.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    
    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
    
    @Column(nullable = false, length = 1000)
    private String questionText;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;
    
    @Column(length = 2000)
    private String options; // JSON array stored as string
    
    @Column(nullable = false)
    private String correctAnswer;
    
    @Column(length = 500)
    private String imageUrl;
    
    @Column(length = 500)
    private String audioUrl;
    
    @Column(length = 1000)
    private String explanation;
    
    @Column(nullable = false)
    private Integer points;
}
