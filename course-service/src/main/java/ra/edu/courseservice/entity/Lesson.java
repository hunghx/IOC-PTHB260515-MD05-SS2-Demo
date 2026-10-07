package ra.edu.courseservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "duration_seconds")
    private int durationSeconds;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "is_preview", nullable = false)
    private boolean isPreview;
}
