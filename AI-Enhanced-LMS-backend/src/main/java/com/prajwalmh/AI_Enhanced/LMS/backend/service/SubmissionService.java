package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.SubmissionRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.SubmissionResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Assignment;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Submission;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.SubmissionStatus;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.AssignmentRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.SubmissionRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public SubmissionResponse submitAssignment(Long assignmentId, SubmissionRequest request) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Selected user is not a student");
        }

        if (submissionRepository.existsByAssignmentAndStudent(assignment, student)) {
            throw new RuntimeException("Student has already submitted this assignment. Use update submission instead.");
        }

        SubmissionStatus status = SubmissionStatus.SUBMITTED;

        if (assignment.getDueDate() != null && LocalDateTime.now().isAfter(assignment.getDueDate())) {
            status = SubmissionStatus.LATE;
        }

        Submission submission = Submission.builder()
                .answerText(request.getAnswerText())
                .fileName(request.getFileName())
                .fileType(request.getFileType())
                .fileSize(request.getFileSize())
                .fileUrl(request.getFileUrl())
                .s3Key(request.getS3Key())
                .status(status)
                .assignment(assignment)
                .student(student)
                .build();

        Submission savedSubmission = submissionRepository.save(submission);

        return mapToResponse(savedSubmission);
    }

    public List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        return submissionRepository.findByAssignment(assignment)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<SubmissionResponse> getSubmissionsByStudent(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return submissionRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SubmissionResponse getSubmissionById(Long submissionId) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        return mapToResponse(submission);
    }

    public SubmissionResponse updateSubmission(Long submissionId, SubmissionRequest request) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        submission.setAnswerText(request.getAnswerText());
        submission.setFileName(request.getFileName());
        submission.setFileType(request.getFileType());
        submission.setFileSize(request.getFileSize());
        submission.setFileUrl(request.getFileUrl());
        submission.setS3Key(request.getS3Key());
        submission.setStatus(SubmissionStatus.RESUBMITTED);

        Submission updatedSubmission = submissionRepository.save(submission);

        return mapToResponse(updatedSubmission);
    }

    public void deleteSubmission(Long submissionId) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        submissionRepository.delete(submission);
    }

    private SubmissionResponse mapToResponse(Submission submission) {

        Assignment assignment = submission.getAssignment();
        User student = submission.getStudent();

        return SubmissionResponse.builder()
                .id(submission.getId())
                .answerText(submission.getAnswerText())
                .fileName(submission.getFileName())
                .fileType(submission.getFileType())
                .fileSize(submission.getFileSize())
                .fileUrl(submission.getFileUrl())
                .s3Key(submission.getS3Key())
                .status(submission.getStatus())
                .assignmentId(assignment != null ? assignment.getId() : null)
                .assignmentTitle(assignment != null ? assignment.getTitle() : null)
                .studentId(student != null ? student.getId() : null)
                .studentName(student != null ? student.getFullName() : null)
                .studentEmail(student != null ? student.getEmail() : null)
                .submittedAt(submission.getSubmittedAt())
                .updatedAt(submission.getUpdatedAt())
                .build();
    }
}