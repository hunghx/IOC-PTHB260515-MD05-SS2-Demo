package ra.edu.enrollmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.edu.enrollmentservice.entity.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByUserIdOrderByEnrolledAtDesc(UUID userId);

    Optional<Enrollment> findByUserIdAndCourseId(UUID userId, UUID courseId);

    boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);
}
