package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.ResourceRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.ResourceResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.CourseModule;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Resource;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseModuleRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.ResourceRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final UserRepository userRepository;

    public ResourceResponse createResource(Long moduleId, ResourceRequest request) {

        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));

        User uploadedBy = userRepository.findById(request.getUploadedById())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUploadedById()));

        if (uploadedBy.getRole() != Role.TEACHER && uploadedBy.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only teacher or admin can upload resources");
        }

        Resource resource = Resource.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileName(request.getFileName())
                .fileType(request.getFileType())
                .fileSize(request.getFileSize())
                .fileUrl(request.getFileUrl())
                .s3Key(request.getS3Key())
                .module(module)
                .uploadedBy(uploadedBy)
                .build();

        Resource savedResource = resourceRepository.save(resource);

        return mapToResponse(savedResource);
    }

    public List<ResourceResponse> getResourcesByModule(Long moduleId) {

        CourseModule module = courseModuleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));

        return resourceRepository.findByModuleOrderByUploadedAtDesc(module)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ResourceResponse getResourceById(Long resourceId) {

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));

        return mapToResponse(resource);
    }

    public void deleteResource(Long resourceId) {

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));

        resourceRepository.delete(resource);
    }

    private ResourceResponse mapToResponse(Resource resource) {

        CourseModule module = resource.getModule();
        Course course = module.getCourse();
        User uploadedBy = resource.getUploadedBy();

        return ResourceResponse.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .fileName(resource.getFileName())
                .fileType(resource.getFileType())
                .fileSize(resource.getFileSize())
                .fileUrl(resource.getFileUrl())
                .s3Key(resource.getS3Key())
                .moduleId(module.getId())
                .moduleTitle(module.getTitle())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .uploadedById(uploadedBy.getId())
                .uploadedByName(uploadedBy.getFullName())
                .uploadedAt(resource.getUploadedAt())
                .build();
    }
}