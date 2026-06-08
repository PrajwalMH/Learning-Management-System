package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacher(User teacher);

    List<Course> findByActiveTrue();

    List<Course> findByTitleContainingIgnoreCase(String title);
}