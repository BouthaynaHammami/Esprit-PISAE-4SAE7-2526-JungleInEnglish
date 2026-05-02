package tn.esprit.language_courses_service.ChildrenEnglish.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelChildren {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer minAge;
    
    @Column(nullable = false)
    private Integer maxAge;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "order_index")
    private Integer orderIndex = 0;
    
    @OneToMany(mappedBy = "levelChildren", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Child> children;
    
    @OneToMany(mappedBy = "levelChildren", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;
}
