package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.CourseModuleRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.CourseModuleResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.CourseModule;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseModuleRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseModuleService {

    private final CourseModuleRepository courseModuleRepository;
    private final CourseRepository courseRepository;

    public CourseModuleResponse createModule(Long courseId, CourseModuleRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        CourseModule module = CourseModule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .orderIndex(request.getOrderIndex())
                .published(request.isPublished())
                .course(course)
                .build();

        CourseModule savedModule = courseModuleRepository.save(module);

        return mapToResponse(savedModule);
    }

    public List<CourseModuleResponse> getModulesByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        return courseModuleRepository.findByCourseOrderByOrderIndexAsc(course)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CourseModuleResponse getModuleById(Long moduleId) {

        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));

        return mapToResponse(module);
    }

    public CourseModuleResponse updateModule(Long moduleId, CourseModuleRequest request) {

        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));

        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setOrderIndex(request.getOrderIndex());
        module.setPublished(request.isPublished());

        CourseModule updatedModule = courseModuleRepository.save(module);

        return mapToResponse(updatedModule);
    }

    public void deleteModule(Long moduleId) {

        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));

        courseModuleRepository.delete(module);
    }

    private CourseModuleResponse mapToResponse(CourseModule module) {

        Course course = module.getCourse();

        return CourseModuleResponse.builder()
                .id(module.getId())
                .title(module.getTitle())
                .description(module.getDescription())
                .orderIndex(module.getOrderIndex())
                .published(module.isPublished())
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .createdAt(module.getCreatedAt())
                .updatedAt(module.getUpdatedAt())
                .build();
    }
}