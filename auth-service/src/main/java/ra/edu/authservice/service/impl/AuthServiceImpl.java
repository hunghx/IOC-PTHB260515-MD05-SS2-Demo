package ra.edu.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.authservice.config.JwtTokenProvider;
import ra.edu.authservice.config.UserPrincipal;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new EmailAlreadyExistsException(cleanEmail);
        }

        User user = User.builder()
                .email(cleanEmail)
                .password(passwordEncoder.encode(request.getPassword()))
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

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cleanEmail, request.getPassword())
            );
        } catch (Exception e) {
            throw new AppException("Email hoặc mật khẩu không chính xác", 401);
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AppException("Tài khoản của bạn đã bị khóa", 403);
        }

        String jwtToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        return LoginResponse.builder()
                .accessToken(jwtToken)
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

        return mapToProfileResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với email: " + email));

        return mapToProfileResponse(user);
    }

    private UserProfileResponse mapToProfileResponse(User user) {
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
}
