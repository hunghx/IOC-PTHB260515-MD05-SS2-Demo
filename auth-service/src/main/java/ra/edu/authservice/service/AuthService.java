package ra.edu.authservice.service;

import ra.edu.authservice.dto.request.LoginRequest;
import ra.edu.authservice.dto.request.RegisterRequest;
import ra.edu.authservice.dto.response.AuthResponse;
import ra.edu.authservice.dto.response.LoginResponse;
import ra.edu.authservice.dto.response.UserProfileResponse;

import java.util.UUID;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserProfileResponse getProfile(UUID userId);

    UserProfileResponse getProfileByEmail(String email);
}
