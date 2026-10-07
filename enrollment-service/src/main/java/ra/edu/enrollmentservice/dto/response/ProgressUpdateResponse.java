package ra.edu.enrollmentservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressUpdateResponse {

    private UUID enrollmentId;
    private UUID courseId;
    private UUID lessonId;
    private boolean isCompleted;
    private BigDecimal currentProgressPercentage;
    private boolean isCourseCompleted;
}
