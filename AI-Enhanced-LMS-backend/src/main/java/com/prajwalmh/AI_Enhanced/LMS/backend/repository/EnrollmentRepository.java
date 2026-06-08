package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Enrollment;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(User student);

    List<Enrollment> findByCourse(Course course);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    boolean existsByStudentAndCourse(User student, Course course);
}