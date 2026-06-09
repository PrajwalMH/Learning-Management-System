package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.CourseRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.CourseResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseResponse createCourse(CourseRequest request) {

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getTeacherId()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Selected user is not a teacher");
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .level(request.getLevel())
                .teacher(teacher)
                .active(true)
                .build();

        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        return mapToResponse(course);
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + request.getTeacherId()));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("Selected user is not a teacher");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setLevel(request.getLevel());
        course.setTeacher(teacher);

        Course updatedCourse = courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }

    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        course.setActive(false);
        courseRepository.save(course);
    }

    private CourseResponse mapToResponse(Course course) {

        User teacher = course.getTeacher();

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .level(course.getLevel())
                .active(course.isActive())
                .teacherId(teacher != null ? teacher.getId() : null)
                .teacherName(teacher != null ? teacher.getFullName() : null)
                .teacherEmail(teacher != null ? teacher.getEmail() : null)
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}