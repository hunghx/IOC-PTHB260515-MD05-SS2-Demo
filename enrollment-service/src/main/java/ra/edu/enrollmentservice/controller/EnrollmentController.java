package ra.edu.enrollmentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.enrollmentservice.dto.request.EnrollmentCreateRequest;
import ra.edu.enrollmentservice.dto.response.ApiResponse;
import ra.edu.enrollmentservice.dto.response.EnrollmentResponse;
import ra.edu.enrollmentservice.dto.response.MyCourseResponse;
import ra.edu.enrollmentservice.dto.response.ProgressUpdateResponse;
import ra.edu.enrollmentservice.service.EnrollmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> createEnrollment(
            @Valid @RequestBody EnrollmentCreateRequest request
    ) {
        EnrollmentResponse response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Ghi danh khóa học thành công"));
    }

    @GetMapping("/my-courses")
    public ResponseEntity<ApiResponse<List<MyCourseResponse>>> getMyCourses(
            @RequestParam("userId") UUID userId
    ) {
        List<MyCourseResponse> response = enrollmentService.getMyCourses(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/courses/{courseId}/lessons/{lessonId}/complete")
    public ResponseEntity<ApiResponse<ProgressUpdateResponse>> completeLesson(
            @PathVariable("courseId") UUID courseId,
            @PathVariable("lessonId") UUID lessonId,
            @RequestParam("userId") UUID userId,
            @RequestParam(value = "totalLessons", defaultValue = "10") int totalLessons
    ) {
        ProgressUpdateResponse response = enrollmentService.completeLesson(courseId, lessonId, userId, totalLessons);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
