package ra.edu.courseservice.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResponse {

    private UUID id;
    private String title;
    private String videoUrl;
    private int durationSeconds;
    private int sortOrder;
    private boolean isPreview;
}
