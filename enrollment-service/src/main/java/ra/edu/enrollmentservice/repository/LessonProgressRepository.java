package ra.edu.enrollmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.enrollmentservice.entity.LessonProgress;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, UUID> {

    Optional<LessonProgress> findByEnrollmentIdAndLessonId(UUID enrollmentId, UUID lessonId);

    long countByEnrollmentIdAndIsCompletedTrue(UUID enrollmentId);
}
