package ra.edu.courseservice.service;

import ra.edu.courseservice.dto.request.CreateCourseRequest;
import ra.edu.courseservice.dto.request.CreateLessonRequest;
import ra.edu.courseservice.dto.request.CreateSectionRequest;
import ra.edu.courseservice.dto.response.*;

import java.util.UUID;

public interface CourseService {

    PageResponse<CourseSummaryResponse> getCourses(String keyword, int page, int size);

    CourseDetailResponse getCourseDetail(UUID courseId);

    CourseDetailResponse createCourse(CreateCourseRequest request);

    SectionResponse createSection(UUID courseId, CreateSectionRequest request);

    LessonResponse createLesson(UUID sectionId, CreateLessonRequest request);
}
