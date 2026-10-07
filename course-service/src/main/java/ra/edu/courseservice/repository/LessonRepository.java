package ra.edu.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.courseservice.entity.Lesson;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findBySectionIdOrderBySortOrderAsc(UUID sectionId);

    long countBySectionCourseId(UUID courseId);
}
