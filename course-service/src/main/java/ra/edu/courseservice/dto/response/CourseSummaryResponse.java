package ra.edu.courseservice.dto.response;

import lombok.*;
import ra.edu.courseservice.constant.CourseStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSummaryResponse {

    private UUID id;
    private UUID instructorId;
    private String title;
    private BigDecimal price;
    private String thumbnailUrl;
    private CourseStatus status;
}
