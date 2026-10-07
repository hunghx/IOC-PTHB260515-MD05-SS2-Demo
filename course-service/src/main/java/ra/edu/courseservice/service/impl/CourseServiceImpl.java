package ra.edu.courseservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.courseservice.constant.CourseConstant;
import ra.edu.courseservice.constant.CourseStatus;
import ra.edu.courseservice.dto.request.CreateCourseRequest;
import ra.edu.courseservice.dto.request.CreateLessonRequest;
import ra.edu.courseservice.dto.request.CreateSectionRequest;
import ra.edu.courseservice.dto.response.*;
import ra.edu.courseservice.entity.Course;
import ra.edu.courseservice.entity.Lesson;
import ra.edu.courseservice.entity.Section;
import ra.edu.courseservice.exception.ResourceNotFoundException;
import ra.edu.courseservice.repository.CourseRepository;
import ra.edu.courseservice.repository.LessonRepository;
import ra.edu.courseservice.repository.SectionRepository;
import ra.edu.courseservice.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CourseSummaryResponse> getCourses(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Course> coursePage = courseRepository.searchCourses(
                keyword != null && !keyword.isBlank() ? keyword.trim() : null,
                CourseStatus.PUBLISHED,
                pageRequest
        );

        List<CourseSummaryResponse> items = coursePage.getContent().stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());

        return PageResponse.<CourseSummaryResponse>builder()
                .items(items)
                .page(coursePage.getNumber())
                .size(coursePage.getSize())
                .totalElements(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseDetail(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học với ID: " + courseId));

        return mapToDetail(course);
    }

    @Override
    @Transactional
    public CourseDetailResponse createCourse(CreateCourseRequest request) {
        Course course = Course.builder()
                .instructorId(request.getInstructorId())
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .thumbnailUrl(request.getThumbnailUrl() != null ? request.getThumbnailUrl() : CourseConstant.DEFAULT_THUMBNAIL)
                .status(CourseStatus.PUBLISHED)
                .build();

        Course savedCourse = courseRepository.save(course);
        return mapToDetail(savedCourse);
    }

    @Override
    @Transactional
    public SectionResponse createSection(UUID courseId, CreateSectionRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học với ID: " + courseId));

        Section section = Section.builder()
                .course(course)
                .title(request.getTitle().trim())
                .sortOrder(request.getSortOrder())
                .build();

        Section savedSection = sectionRepository.save(section);
        return mapToSectionResponse(savedSection);
    }

    @Override
    @Transactional
    public LessonResponse createLesson(UUID sectionId, CreateLessonRequest request) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chương học với ID: " + sectionId));

        Lesson lesson = Lesson.builder()
                .section(section)
                .title(request.getTitle().trim())
                .videoUrl(request.getVideoUrl())
                .durationSeconds(request.getDurationSeconds())
                .sortOrder(request.getSortOrder())
                .isPreview(request.isPreview())
                .build();

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToLessonResponse(savedLesson);
    }

    private CourseSummaryResponse mapToSummary(Course course) {
        return CourseSummaryResponse.builder()
                .id(course.getId())
                .instructorId(course.getInstructorId())
                .title(course.getTitle())
                .price(course.getPrice())
                .thumbnailUrl(course.getThumbnailUrl())
                .status(course.getStatus())
                .build();
    }

    private CourseDetailResponse mapToDetail(Course course) {
        List<SectionResponse> sectionResponses = course.getSections() != null
                ? course.getSections().stream().map(this::mapToSectionResponse).collect(Collectors.toList())
                : new ArrayList<>();

        return CourseDetailResponse.builder()
                .id(course.getId())
                .instructorId(course.getInstructorId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .thumbnailUrl(course.getThumbnailUrl())
                .status(course.getStatus())
                .sections(sectionResponses)
                .build();
    }

    private SectionResponse mapToSectionResponse(Section section) {
        List<LessonResponse> lessonResponses = section.getLessons() != null
                ? section.getLessons().stream().map(this::mapToLessonResponse).collect(Collectors.toList())
                : new ArrayList<>();

        return SectionResponse.builder()
                .id(section.getId())
                .title(section.getTitle())
                .sortOrder(section.getSortOrder())
                .lessons(lessonResponses)
                .build();
    }

    private LessonResponse mapToLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .videoUrl(lesson.getVideoUrl())
                .durationSeconds(lesson.getDurationSeconds())
                .sortOrder(lesson.getSortOrder())
                .isPreview(lesson.isPreview())
                .build();
    }
}
