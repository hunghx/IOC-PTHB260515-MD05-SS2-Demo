package ra.edu.courseservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCourseRequest {

    private UUID instructorId;

    @NotBlank(message = "Tiêu đề khóa học không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Giá khóa học không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá khóa học không được âm")
    private BigDecimal price;

    private String thumbnailUrl;
}
