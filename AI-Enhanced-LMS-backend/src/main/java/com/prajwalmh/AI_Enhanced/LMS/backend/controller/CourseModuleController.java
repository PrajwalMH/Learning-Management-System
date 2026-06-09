package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.CourseModuleRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.CourseModuleResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.CourseModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CourseModuleController {

    private final CourseModuleService courseModuleService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<CourseModuleResponse> createModule(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseModuleRequest request
    ) {
        CourseModuleResponse response = courseModuleService.createModule(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<?> getModulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseModuleService.getModulesByCourse(courseId));
    }

    @GetMapping("/modules/{moduleId}")
    public ResponseEntity<CourseModuleResponse> getModuleById(@PathVariable Long moduleId) {
        return ResponseEntity.ok(courseModuleService.getModuleById(moduleId));
    }

    @PutMapping("/modules/{moduleId}")
    public ResponseEntity<CourseModuleResponse> updateModule(
            @PathVariable Long moduleId,
            @Valid @RequestBody CourseModuleRequest request
    ) {
        return ResponseEntity.ok(courseModuleService.updateModule(moduleId, request));
    }

    @DeleteMapping("/modules/{moduleId}")
    public ResponseEntity<String> deleteModule(@PathVariable Long moduleId) {
        courseModuleService.deleteModule(moduleId);
        return ResponseEntity.ok("Module deleted successfully");
    }
}