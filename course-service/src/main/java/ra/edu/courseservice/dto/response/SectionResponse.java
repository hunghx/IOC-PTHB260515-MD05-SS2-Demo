package ra.edu.courseservice.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponse {

    private UUID id;
    private String title;
    private int sortOrder;
    private List<LessonResponse> lessons;
}
