package ra.edu.courseservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.edu.courseservice.constant.CourseStatus;
import ra.edu.courseservice.entity.Course;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT c FROM Course c WHERE c.status = :status AND " +
           "(:keyword IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Course> searchCourses(@Param("keyword") String keyword, @Param("status") CourseStatus status, Pageable pageable);
}
