package ra.edu.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.authservice.constant.AuthConstant;
import ra.edu.authservice.constant.UserStatus;
import ra.edu.authservice.dto.request.LoginRequest;
import ra.edu.authservice.dto.request.RegisterRequest;
import ra.edu.authservice.dto.response.AuthResponse;
import ra.edu.authservice.dto.response.LoginResponse;
import ra.edu.authservice.dto.response.UserProfileResponse;
import ra.edu.authservice.entity.User;
import ra.edu.authservice.exception.AppException;
import ra.edu.authservice.exception.EmailAlreadyExistsException;
import ra.edu.authservice.exception.ResourceNotFoundException;
import ra.edu.authservice.repository.UserRepository;
import ra.edu.authservice.service.AuthService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new EmailAlreadyExistsException(cleanEmail);
        }

        User user = User.builder()
                .email(cleanEmail)
                .password(hashPassword(request.getPassword()))
                .fullName(request.getFullName().trim())
                .avatarUrl(AuthConstant.DEFAULT_AVATAR)
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(cleanEmail)
                .orElseThrow(() -> new AppException("Email hoặc mật khẩu không chính xác", 401));

        if (!user.getPassword().equals(hashPassword(request.getPassword()))) {
            throw new AppException("Email hoặc mật khẩu không chính xác", 401);
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AppException("Tài khoản của bạn đã bị khóa", 403);
        }

        // Tạo JWT Token giả lập cơ bản cho Microservice
        String simulatedToken = Base64.getEncoder().encodeToString(
                (user.getId() + ":" + user.getEmail() + ":" + user.getRole()).getBytes(StandardCharsets.UTF_8)
        );

        return LoginResponse.builder()
                .accessToken(simulatedToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(LoginResponse.UserInfo.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole())
                        .build())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + userId));

        return UserProfileResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            return rawPassword;
        }
    }
}
