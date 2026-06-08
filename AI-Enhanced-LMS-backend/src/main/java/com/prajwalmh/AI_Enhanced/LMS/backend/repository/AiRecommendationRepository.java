package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.AiRecommendation;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiRecommendationRepository extends JpaRepository<AiRecommendation, Long> {

    List<AiRecommendation> findByStudentOrderByGeneratedAtDesc(User student);

    List<AiRecommendation> findByStudentAndCourseOrderByGeneratedAtDesc(User student, Course course);

    List<AiRecommendation> findByStudentAndCompletedFalseOrderByGeneratedAtDesc(User student);
}