package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.ProgressUpdateRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.ProgressResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Progress;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.ProgressRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public List<ProgressResponse> getProgressByStudent(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Selected user is not a student");
        }

        return progressRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProgressResponse getProgressByStudentAndCourse(Long studentId, Long courseId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Progress progress = progressRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("Progress not found for student id "
                        + studentId + " and course id " + courseId));

        return mapToResponse(progress);
    }

    public ProgressResponse updateProgress(Long studentId, Long courseId, ProgressUpdateRequest request) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Selected user is not a student");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Progress progress = progressRepository.findByStudentAndCourse(student, course)
                .orElseGet(() -> Progress.builder()
                        .student(student)
                        .course(course)
                        .completedModules(0)
                        .totalModules(0)
                        .progressPercentage(0.0)
                        .averageScore(0.0)
                        .lastAccessedAt(LocalDateTime.now())
                        .build());

        if (request.getCompletedModules() != null) {
            progress.setCompletedModules(request.getCompletedModules());
        }

        if (request.getTotalModules() != null) {
            progress.setTotalModules(request.getTotalModules());
        }

        if (request.getProgressPercentage() != null) {
            progress.setProgressPercentage(request.getProgressPercentage());
        } else {
            progress.setProgressPercentage(calculateProgressPercentage(
                    progress.getCompletedModules(),
                    progress.getTotalModules()
            ));
        }

        if (request.getAverageScore() != null) {
            progress.setAverageScore(request.getAverageScore());
        }

        progress.setLastAccessedAt(LocalDateTime.now());

        Progress updatedProgress = progressRepository.save(progress);

        return mapToResponse(updatedProgress);
    }

    private Double calculateProgressPercentage(Integer completedModules, Integer totalModules) {

        if (completedModules == null || totalModules == null || totalModules == 0) {
            return 0.0;
        }

        double percentage = ((double) completedModules / totalModules) * 100.0;

        return Math.round(percentage * 100.0) / 100.0;
    }

    private String calculatePerformanceLevel(Double averageScore) {

        if (averageScore == null) {
            return "NOT_STARTED";
        }

        if (averageScore >= 90) {
            return "EXCELLENT";
        } else if (averageScore >= 80) {
            return "GOOD";
        } else if (averageScore >= 70) {
            return "AVERAGE";
        } else if (averageScore >= 50) {
            return "NEEDS_IMPROVEMENT";
        } else {
            return "AT_RISK";
        }
    }

    private ProgressResponse mapToResponse(Progress progress) {

        User student = progress.getStudent();
        Course course = progress.getCourse();

        return ProgressResponse.builder()
                .id(progress.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .studentEmail(student.getEmail())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .completedModules(progress.getCompletedModules())
                .totalModules(progress.getTotalModules())
                .progressPercentage(progress.getProgressPercentage())
                .averageScore(progress.getAverageScore())
                .performanceLevel(calculatePerformanceLevel(progress.getAverageScore()))
                .lastAccessedAt(progress.getLastAccessedAt())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
}