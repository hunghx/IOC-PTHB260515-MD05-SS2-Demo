package ra.edu.enrollmentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.enrollmentservice.constant.EnrollmentStatus;
import ra.edu.enrollmentservice.dto.request.EnrollmentCreateRequest;
import ra.edu.enrollmentservice.dto.response.EnrollmentResponse;
import ra.edu.enrollmentservice.dto.response.MyCourseResponse;
import ra.edu.enrollmentservice.dto.response.ProgressUpdateResponse;
import ra.edu.enrollmentservice.entity.Enrollment;
import ra.edu.enrollmentservice.entity.LessonProgress;
import ra.edu.enrollmentservice.exception.EnrollmentAlreadyExistsException;
import ra.edu.enrollmentservice.exception.ResourceNotFoundException;
import ra.edu.enrollmentservice.repository.EnrollmentRepository;
import ra.edu.enrollmentservice.repository.LessonProgressRepository;
import ra.edu.enrollmentservice.service.EnrollmentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LessonProgressRepository lessonProgressRepository;

    @Override
    @Transactional
    public EnrollmentResponse createEnrollment(EnrollmentCreateRequest request) {
        if (enrollmentRepository.existsByUserIdAndCourseId(request.getUserId(), request.getCourseId())) {
            throw new EnrollmentAlreadyExistsException("Học viên đã đăng ký khóa học này trước đó.");
        }

        Enrollment enrollment = Enrollment.builder()
                .userId(request.getUserId())
                .courseId(request.getCourseId())
                .status(EnrollmentStatus.ACTIVE)
                .progressPercentage(BigDecimal.ZERO)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapToResponse(savedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyCourseResponse> getMyCourses(UUID userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserIdOrderByEnrolledAtDesc(userId);
        return enrollments.stream().map(e -> MyCourseResponse.builder()
                .enrollmentId(e.getId())
                .courseId(e.getCourseId())
                .status(e.getStatus())
                .progressPercentage(e.getProgressPercentage())
                .enrolledAt(e.getEnrolledAt())
                .completedAt(e.getCompletedAt())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProgressUpdateResponse completeLesson(UUID courseId, UUID lessonId, UUID userId, int totalLessonsInCourse) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Học viên chưa đăng ký khóa học này"));

        Optional<LessonProgress> existingProgress = lessonProgressRepository.findByEnrollmentIdAndLessonId(enrollment.getId(), lessonId);

        if (existingProgress.isEmpty()) {
            LessonProgress progress = LessonProgress.builder()
                    .enrollment(enrollment)
                    .lessonId(lessonId)
                    .isCompleted(true)
                    .completedAt(LocalDateTime.now())
                    .build();
            lessonProgressRepository.save(progress);
        }

        long completedCount = lessonProgressRepository.countByEnrollmentIdAndIsCompletedTrue(enrollment.getId());

        int total = Math.max(totalLessonsInCourse, 1);
        BigDecimal progressPercentage = BigDecimal.valueOf(completedCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);

        if (progressPercentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            progressPercentage = BigDecimal.valueOf(100);
        }

        enrollment.setProgressPercentage(progressPercentage);

        boolean isCompleted = progressPercentage.compareTo(BigDecimal.valueOf(100)) >= 0;
        if (isCompleted && enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        enrollmentRepository.save(enrollment);

        return ProgressUpdateResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(courseId)
                .lessonId(lessonId)
                .isCompleted(true)
                .currentProgressPercentage(progressPercentage)
                .isCourseCompleted(isCompleted)
                .build();
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getId())
                .userId(enrollment.getUserId())
                .courseId(enrollment.getCourseId())
                .status(enrollment.getStatus())
                .progressPercentage(enrollment.getProgressPercentage())
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }
}
