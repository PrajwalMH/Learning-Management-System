package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.AiRecommendationRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.GradeRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.GradeResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.*;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private static final double AI_RECOMMENDATION_THRESHOLD = 70.0;

    private final GradeRepository gradeRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;
    private final ProgressRepository progressRepository;
    private final AiRecommendationService aiRecommendationService;

    public GradeResponse gradeSubmission(Long submissionId, GradeRequest request) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with id: " + submissionId));

        if (gradeRepository.existsBySubmission(submission)) {
            throw new RuntimeException("This submission is already graded. Use update grade instead.");
        }

        User teacher = userRepository.findById(request.getGradedById())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getGradedById()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Selected user is not a teacher");
        }

        Assignment assignment = submission.getAssignment();

        if (request.getMarksObtained() > assignment.getMaxMarks()) {
            throw new RuntimeException("Marks obtained cannot be greater than max marks");
        }

        Grade grade = Grade.builder()
                .marksObtained(request.getMarksObtained())
                .feedback(request.getFeedback())
                .submission(submission)
                .gradedBy(teacher)
                .build();

        Grade savedGrade = gradeRepository.save(grade);

        submission.setStatus(SubmissionStatus.GRADED);
        submissionRepository.save(submission);

        updateStudentProgressAverage(submission.getStudent(), assignment.getCourse());

        generateAiRecommendationIfLowScore(submission, savedGrade);

        return mapToResponse(savedGrade);
    }

    public List<GradeResponse> getGradesByStudent(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return gradeRepository.findBySubmissionStudent(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<GradeResponse> getGradesByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        List<Assignment> assignments = assignmentRepository.findByCourse(course);

        List<GradeResponse> gradeResponses = new ArrayList<>();

        for (Assignment assignment : assignments) {
            List<Submission> submissions = submissionRepository.findByAssignment(assignment);

            for (Submission submission : submissions) {
                gradeRepository.findBySubmission(submission)
                        .ifPresent(grade -> gradeResponses.add(mapToResponse(grade)));
            }
        }

        return gradeResponses;
    }

    public GradeResponse getGradeById(Long gradeId) {

        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        return mapToResponse(grade);
    }

    public GradeResponse updateGrade(Long gradeId, GradeRequest request) {

        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        User teacher = userRepository.findById(request.getGradedById())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getGradedById()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Selected user is not a teacher");
        }

        Assignment assignment = grade.getSubmission().getAssignment();

        if (request.getMarksObtained() > assignment.getMaxMarks()) {
            throw new RuntimeException("Marks obtained cannot be greater than max marks");
        }

        grade.setMarksObtained(request.getMarksObtained());
        grade.setFeedback(request.getFeedback());
        grade.setGradedBy(teacher);

        Grade updatedGrade = gradeRepository.save(grade);

        updateStudentProgressAverage(
                grade.getSubmission().getStudent(),
                grade.getSubmission().getAssignment().getCourse()
        );

        generateAiRecommendationIfLowScore(updatedGrade.getSubmission(), updatedGrade);

        return mapToResponse(updatedGrade);
    }

    private void generateAiRecommendationIfLowScore(Submission submission, Grade grade) {

        Assignment assignment = submission.getAssignment();

        if (assignment == null || assignment.getMaxMarks() == null || assignment.getMaxMarks() <= 0) {
            return;
        }

        if (grade.getMarksObtained() == null) {
            return;
        }

        double percentage = (grade.getMarksObtained() / assignment.getMaxMarks()) * 100.0;

        if (percentage < AI_RECOMMENDATION_THRESHOLD) {

            AiRecommendationRequest request = new AiRecommendationRequest();
            request.setStudentId(submission.getStudent().getId());
            request.setCourseId(assignment.getCourse().getId());
            request.setWeakTopic(assignment.getTitle());
            request.setScore(percentage);

            try {
                aiRecommendationService.generateRecommendations(request);
            } catch (Exception ex) {
                System.out.println("AI recommendation generation failed: " + ex.getMessage());
            }
        }
    }

    private void updateStudentProgressAverage(User student, Course course) {

        List<Grade> studentGrades = gradeRepository.findBySubmissionStudent(student);

        List<Grade> courseGrades = studentGrades.stream()
                .filter(grade -> grade.getSubmission()
                        .getAssignment()
                        .getCourse()
                        .getId()
                        .equals(course.getId()))
                .toList();

        double averageScore = 0.0;

        if (!courseGrades.isEmpty()) {
            averageScore = courseGrades.stream()
                    .mapToDouble(grade -> {
                        double marks = grade.getMarksObtained();
                        double maxMarks = grade.getSubmission().getAssignment().getMaxMarks();
                        return (marks / maxMarks) * 100.0;
                    })
                    .average()
                    .orElse(0.0);
        }

        Progress progress = progressRepository.findByStudentAndCourse(student, course)
                .orElseGet(() -> Progress.builder()
                        .student(student)
                        .course(course)
                        .completedModules(0)
                        .totalModules(0)
                        .progressPercentage(0.0)
                        .averageScore(0.0)
                        .build());

        progress.setAverageScore(averageScore);

        progressRepository.save(progress);
    }

    private GradeResponse mapToResponse(Grade grade) {

        Submission submission = grade.getSubmission();
        Assignment assignment = submission.getAssignment();
        Course course = assignment.getCourse();
        User student = submission.getStudent();
        User gradedBy = grade.getGradedBy();

        Double percentage = 0.0;

        if (assignment.getMaxMarks() != null && assignment.getMaxMarks() > 0) {
            percentage = (grade.getMarksObtained() / assignment.getMaxMarks()) * 100.0;
        }

        return GradeResponse.builder()
                .id(grade.getId())
                .marksObtained(grade.getMarksObtained())
                .maxMarks(assignment.getMaxMarks())
                .percentage(percentage)
                .feedback(grade.getFeedback())
                .submissionId(submission.getId())
                .assignmentId(assignment.getId())
                .assignmentTitle(assignment.getTitle())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .studentEmail(student.getEmail())
                .gradedById(gradedBy.getId())
                .gradedByName(gradedBy.getFullName())
                .gradedAt(grade.getGradedAt())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }
}