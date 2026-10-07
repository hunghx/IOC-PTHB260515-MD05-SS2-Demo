package ra.edu.courseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.courseservice.dto.request.CreateCourseRequest;
import ra.edu.courseservice.dto.request.CreateLessonRequest;
import ra.edu.courseservice.dto.request.CreateSectionRequest;
import ra.edu.courseservice.dto.response.*;
import ra.edu.courseservice.service.CourseService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseSummaryResponse>>> getCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        PageResponse<CourseSummaryResponse> response = courseService.getCourses(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseDetail(@PathVariable("id") UUID id) {
        CourseDetailResponse response = courseService.getCourseDetail(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDetailResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request
    ) {
        CourseDetailResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Tạo khóa học thành công"));
    }

    @PostMapping("/{id}/sections")
    public ResponseEntity<ApiResponse<SectionResponse>> createSection(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CreateSectionRequest request
    ) {
        SectionResponse response = courseService.createSection(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Thêm chương học thành công"));
    }

    @PostMapping("/sections/{sectionId}/lessons")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(
            @PathVariable("sectionId") UUID sectionId,
            @Valid @RequestBody CreateLessonRequest request
    ) {
        LessonResponse response = courseService.createLesson(sectionId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response, "Thêm bài học thành công"));
    }
}
