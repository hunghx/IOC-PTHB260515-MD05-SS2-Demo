package ra.edu.enrollmentservice.service;

import ra.edu.enrollmentservice.dto.request.EnrollmentCreateRequest;
import ra.edu.enrollmentservice.dto.response.EnrollmentResponse;
import ra.edu.enrollmentservice.dto.response.MyCourseResponse;
import ra.edu.enrollmentservice.dto.response.ProgressUpdateResponse;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    EnrollmentResponse createEnrollment(EnrollmentCreateRequest request);

    List<MyCourseResponse> getMyCourses(UUID userId);

    ProgressUpdateResponse completeLesson(UUID courseId, UUID lessonId, UUID userId, int totalLessonsInCourse);
}
