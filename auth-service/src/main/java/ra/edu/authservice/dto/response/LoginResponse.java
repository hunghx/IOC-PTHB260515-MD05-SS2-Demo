package ra.edu.authservice.dto.response;

import lombok.*;
import ra.edu.authservice.constant.UserRole;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UserInfo user;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private UUID userId;
        private String email;
        private String fullName;
        private UserRole role;
    }
}
