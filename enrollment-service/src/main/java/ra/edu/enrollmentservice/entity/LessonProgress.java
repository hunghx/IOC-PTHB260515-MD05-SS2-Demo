package ra.edu.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lesson_progresses", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"enrollment_id", "lesson_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(name = "lesson_id", nullable = false)
    private UUID lessonId;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        if (this.isCompleted && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }
}
