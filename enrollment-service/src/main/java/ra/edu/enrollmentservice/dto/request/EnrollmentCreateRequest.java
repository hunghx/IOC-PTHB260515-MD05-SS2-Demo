package ra.edu.enrollmentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentCreateRequest {

    @NotNull(message = "userId không được để trống")
    private UUID userId;

    @NotNull(message = "courseId không được để trống")
    private UUID courseId;
}
