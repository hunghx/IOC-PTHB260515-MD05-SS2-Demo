package ra.edu.courseservice.dto.response;

import lombok.*;
import ra.edu.courseservice.constant.CourseStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDetailResponse {

    private UUID id;
    private UUID instructorId;
    private String title;
    private String description;
    private BigDecimal price;
    private String thumbnailUrl;
    private CourseStatus status;
    private List<SectionResponse> sections;
}
