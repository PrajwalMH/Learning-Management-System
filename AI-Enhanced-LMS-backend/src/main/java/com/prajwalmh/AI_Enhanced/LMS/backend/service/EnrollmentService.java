package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.EnrollmentRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.EnrollmentResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Enrollment;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Progress;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseModuleRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.EnrollmentRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.ProgressRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ProgressRepository progressRepository;
    private final CourseModuleRepository courseModuleRepository;

    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Selected user is not a student");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .active(true)
                .build();

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        int totalModules = courseModuleRepository.findByCourseOrderByOrderIndexAsc(course).size();

        Progress progress = Progress.builder()
                .student(student)
                .course(course)
                .completedModules(0)
                .totalModules(totalModules)
                .progressPercentage(0.0)
                .averageScore(0.0)
                .build();

        progressRepository.save(progress);

        return mapToResponse(savedEnrollment);
    }

    public List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return enrollmentRepository.findByStudent(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        return enrollmentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deactivateEnrollment(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + enrollmentId));

        enrollment.setActive(false);
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {

        User student = enrollment.getStudent();
        Course course = enrollment.getCourse();

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(student != null ? student.getId() : null)
                .studentName(student != null ? student.getFullName() : null)
                .studentEmail(student != null ? student.getEmail() : null)
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .active(enrollment.isActive())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}