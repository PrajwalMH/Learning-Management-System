package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Quiz;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

    List<QuizQuestion> findByQuizOrderByQuestionOrderAsc(Quiz quiz);
}