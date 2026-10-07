package ra.edu.enrollmentservice.dto.response;

import lombok.*;
import ra.edu.enrollmentservice.constant.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyCourseResponse {

    private UUID enrollmentId;
    private UUID courseId;
    private EnrollmentStatus status;
    private BigDecimal progressPercentage;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}
