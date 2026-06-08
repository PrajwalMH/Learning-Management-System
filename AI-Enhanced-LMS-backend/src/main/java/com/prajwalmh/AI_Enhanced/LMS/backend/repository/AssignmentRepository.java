package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Assignment;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByCourse(Course course);

    List<Assignment> findByCourseAndPublishedTrue(Course course);
}