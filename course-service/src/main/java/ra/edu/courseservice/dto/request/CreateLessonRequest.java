package ra.edu.courseservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLessonRequest {

    @NotBlank(message = "Tiêu đề bài học không được để trống")
    private String title;

    private String videoUrl;

    private int durationSeconds;

    private int sortOrder;

    private boolean isPreview;
}
