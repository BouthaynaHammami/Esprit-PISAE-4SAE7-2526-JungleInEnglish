package tn.esprit.language_courses_service.ChildrenEnglish.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Progress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;
    
    @Column(nullable = false)
    private Float completionRate;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date lastAccess;
    
    @ManyToOne
    @JoinColumn(name = "child_id", nullable = true)
    private Child child;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private Course course;
}
