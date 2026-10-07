package ra.edu.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.authservice.dto.request.LoginRequest;
import ra.edu.authservice.dto.request.RegisterRequest;
import ra.edu.authservice.dto.response.ApiResponse;
import ra.edu.authservice.dto.response.AuthResponse;
import ra.edu.authservice.dto.response.LoginResponse;
import ra.edu.authservice.dto.response.UserProfileResponse;
import ra.edu.authservice.service.AuthService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Đăng ký tài khoản thành công"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(@RequestParam("userId") UUID userId) {
        UserProfileResponse response = authService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
