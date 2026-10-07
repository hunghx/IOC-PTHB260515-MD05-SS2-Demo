package ra.edu.authservice.dto.response;

import lombok.*;
import ra.edu.authservice.constant.UserRole;
import ra.edu.authservice.constant.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private UUID userId;
    private String email;
    private String fullName;
    private String avatarUrl;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
}
