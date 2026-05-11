package tn.esprit.language_courses_service.ChildrenEnglish.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Child {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long childId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Parent parent;

    // Flat parentId (userId from auth service) for direct frontend filtering
    @Column(name = "parent_user_id", nullable = true)
    private Long parentId;

    // Avatar emoji chosen by the parent
    @Column(nullable = true)
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = true)
    private LevelChildren levelChildren;
    
    @Column(nullable = false)
    private Integer xp = 0;
    
    @Column(nullable = false)
    private Integer level = 1;
    
    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Progress> progressList;
}
