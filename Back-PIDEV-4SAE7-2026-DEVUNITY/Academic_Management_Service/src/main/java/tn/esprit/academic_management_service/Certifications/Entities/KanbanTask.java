package tn.esprit.academic_management_service.Certifications.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "certif_kanban_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KanbanTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private KanbanStatus status;

    private Long userId;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /** position within the column for drag & drop ordering */
    private Integer position;

    /** true once the 15-min reminder has been sent */
    private Boolean reminderSent;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = KanbanStatus.TODO;
        if (this.reminderSent == null) this.reminderSent = false;
        if (this.position == null) this.position = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
