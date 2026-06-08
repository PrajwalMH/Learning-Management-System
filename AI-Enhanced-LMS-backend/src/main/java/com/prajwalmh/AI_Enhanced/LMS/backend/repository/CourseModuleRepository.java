package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {

    List<CourseModule> findByCourseOrderByOrderIndexAsc(Course course);

    List<CourseModule> findByCourseAndPublishedTrueOrderByOrderIndexAsc(Course course);
}