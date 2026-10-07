package ra.edu.authservice.dto.response;

import lombok.*;
import ra.edu.authservice.constant.UserRole;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private UUID userId;
    private String email;
    private String fullName;
    private UserRole role;
}
