package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Quiz;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.QuizAttempt;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findByStudent(User student);

    List<QuizAttempt> findByQuiz(Quiz quiz);

    Optional<QuizAttempt> findByQuizAndStudent(Quiz quiz, User student);

    boolean existsByQuizAndStudent(Quiz quiz, User student);
}