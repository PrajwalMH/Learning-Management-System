package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.CourseModule;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    List<Quiz> findByCourse(Course course);

    List<Quiz> findByCourseAndPublishedTrue(Course course);

    List<Quiz> findByModule(CourseModule module);
}