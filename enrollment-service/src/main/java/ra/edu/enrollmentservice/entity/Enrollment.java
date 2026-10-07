package ra.edu.enrollmentservice.entity;

import jakarta.persistence.*;
import lombok.*;
import ra.edu.enrollmentservice.constant.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EnrollmentStatus status;

    @Column(name = "progress_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal progressPercentage;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LessonProgress> lessonProgresses = new ArrayList<>();

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        this.enrolledAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = EnrollmentStatus.ACTIVE;
        }
        if (this.progressPercentage == null) {
            this.progressPercentage = BigDecimal.ZERO;
        }
    }
}
