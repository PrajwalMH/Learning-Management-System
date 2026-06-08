package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Grade;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Submission;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findBySubmission(Submission submission);

    List<Grade> findBySubmissionStudent(User student);

    boolean existsBySubmission(Submission submission);
}