package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.AssignmentRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.AssignmentResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Assignment;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.AssignmentRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public AssignmentResponse createAssignment(Long courseId, AssignmentRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        User teacher = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getCreatedById()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Selected user is not a teacher");
        }

        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .maxMarks(request.getMaxMarks())
                .dueDate(request.getDueDate())
                .published(request.isPublished())
                .course(course)
                .createdBy(teacher)
                .build();

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return mapToResponse(savedAssignment);
    }

    public List<AssignmentResponse> getAssignmentsByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        return assignmentRepository.findByCourse(course)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssignmentResponse getAssignmentById(Long assignmentId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        return mapToResponse(assignment);
    }

    public AssignmentResponse updateAssignment(Long assignmentId, AssignmentRequest request) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        User teacher = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getCreatedById()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Selected user is not a teacher");
        }

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setMaxMarks(request.getMaxMarks());
        assignment.setDueDate(request.getDueDate());
        assignment.setPublished(request.isPublished());
        assignment.setCreatedBy(teacher);

        Assignment updatedAssignment = assignmentRepository.save(assignment);

        return mapToResponse(updatedAssignment);
    }

    public void deleteAssignment(Long assignmentId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        assignmentRepository.delete(assignment);
    }

    private AssignmentResponse mapToResponse(Assignment assignment) {

        Course course = assignment.getCourse();
        User createdBy = assignment.getCreatedBy();

        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .maxMarks(assignment.getMaxMarks())
                .dueDate(assignment.getDueDate())
                .published(assignment.isPublished())
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .createdById(createdBy != null ? createdBy.getId() : null)
                .createdByName(createdBy != null ? createdBy.getFullName() : null)
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}