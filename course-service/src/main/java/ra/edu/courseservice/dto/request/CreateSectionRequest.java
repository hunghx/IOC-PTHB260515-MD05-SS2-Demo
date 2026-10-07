package ra.edu.courseservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSectionRequest {

    @NotBlank(message = "Tiêu đề chương học không được để trống")
    private String title;

    private int sortOrder;
}
