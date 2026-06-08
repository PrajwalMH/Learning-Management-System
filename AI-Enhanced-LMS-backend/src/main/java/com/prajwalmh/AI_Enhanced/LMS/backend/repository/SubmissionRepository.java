package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Assignment;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Submission;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByStudent(User student);

    List<Submission> findByAssignment(Assignment assignment);

    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, User student);

    boolean existsByAssignmentAndStudent(Assignment assignment, User student);
}