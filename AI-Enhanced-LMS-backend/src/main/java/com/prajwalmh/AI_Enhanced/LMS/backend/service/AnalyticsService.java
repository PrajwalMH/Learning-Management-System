package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.CourseAnalyticsResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.GradeDistributionResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.StudentTrendResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.*;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    public CourseAnalyticsResponse getCourseSummary(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        List<Assignment> assignments = assignmentRepository.findByCourse(course);

        List<Submission> submissions = new ArrayList<>();
        List<Grade> grades = new ArrayList<>();

        for (Assignment assignment : assignments) {
            List<Submission> assignmentSubmissions = submissionRepository.findByAssignment(assignment);
            submissions.addAll(assignmentSubmissions);

            for (Submission submission : assignmentSubmissions) {
                gradeRepository.findBySubmission(submission).ifPresent(grades::add);
            }
        }

        List<Double> percentages = grades.stream()
                .map(this::calculatePercentage)
                .toList();

        double classAverage = percentages.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double highestScore = percentages.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0.0);

        double lowestScore = percentages.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0.0);

        long excellentCount = percentages.stream().filter(score -> score >= 90).count();
        long goodCount = percentages.stream().filter(score -> score >= 75 && score < 90).count();
        long averageCount = percentages.stream().filter(score -> score >= 60 && score < 75).count();
        long weakCount = percentages.stream().filter(score -> score < 60).count();

        return CourseAnalyticsResponse.builder()
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .totalAssignments((long) assignments.size())
                .totalSubmissions((long) submissions.size())
                .totalGradedSubmissions((long) grades.size())
                .classAverage(round(classAverage))
                .highestScore(round(highestScore))
                .lowestScore(round(lowestScore))
                .excellentCount(excellentCount)
                .goodCount(goodCount)
                .averageCount(averageCount)
                .weakCount(weakCount)
                .build();
    }

    public List<GradeDistributionResponse> getGradeDistribution(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        List<Assignment> assignments = assignmentRepository.findByCourse(course);

        List<Grade> grades = new ArrayList<>();

        for (Assignment assignment : assignments) {
            List<Submission> submissions = submissionRepository.findByAssignment(assignment);

            for (Submission submission : submissions) {
                gradeRepository.findBySubmission(submission).ifPresent(grades::add);
            }
        }

        List<Double> percentages = grades.stream()
                .map(this::calculatePercentage)
                .toList();

        long range0To40 = percentages.stream().filter(score -> score >= 0 && score <= 40).count();
        long range41To60 = percentages.stream().filter(score -> score > 40 && score <= 60).count();
        long range61To80 = percentages.stream().filter(score -> score > 60 && score <= 80).count();
        long range81To100 = percentages.stream().filter(score -> score > 80 && score <= 100).count();

        List<GradeDistributionResponse> distribution = new ArrayList<>();

        distribution.add(GradeDistributionResponse.builder()
                .range("0-40")
                .count(range0To40)
                .build());

        distribution.add(GradeDistributionResponse.builder()
                .range("41-60")
                .count(range41To60)
                .build());

        distribution.add(GradeDistributionResponse.builder()
                .range("61-80")
                .count(range61To80)
                .build());

        distribution.add(GradeDistributionResponse.builder()
                .range("81-100")
                .count(range81To100)
                .build());

        return distribution;
    }

    public List<StudentTrendResponse> getStudentTrend(Long studentId, Long courseId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        List<Grade> studentGrades = gradeRepository.findBySubmissionStudent(student)
                .stream()
                .filter(grade -> grade.getSubmission()
                        .getAssignment()
                        .getCourse()
                        .getId()
                        .equals(course.getId()))
                .sorted(Comparator.comparing(grade -> grade.getSubmission().getAssignment().getCreatedAt()))
                .toList();

        List<StudentTrendResponse> trendResponses = new ArrayList<>();

        Double previousPercentage = null;

        for (Grade grade : studentGrades) {

            Assignment assignment = grade.getSubmission().getAssignment();

            double percentage = calculatePercentage(grade);

            String trendDirection = "SAME";
            String arrow = "→";

            if (previousPercentage != null) {
                if (percentage > previousPercentage) {
                    trendDirection = "UP";
                    arrow = "↑";
                } else if (percentage < previousPercentage) {
                    trendDirection = "DOWN";
                    arrow = "↓";
                }
            }

            trendResponses.add(StudentTrendResponse.builder()
                    .assignmentId(assignment.getId())
                    .assignmentTitle(assignment.getTitle())
                    .marksObtained(grade.getMarksObtained())
                    .maxMarks(assignment.getMaxMarks())
                    .percentage(round(percentage))
                    .trendDirection(trendDirection)
                    .arrow(arrow)
                    .build());

            previousPercentage = percentage;
        }

        return trendResponses;
    }

    private double calculatePercentage(Grade grade) {
        Double marks = grade.getMarksObtained();
        Double maxMarks = grade.getSubmission().getAssignment().getMaxMarks();

        if (maxMarks == null || maxMarks == 0) {
            return 0.0;
        }

        return (marks / maxMarks) * 100.0;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}