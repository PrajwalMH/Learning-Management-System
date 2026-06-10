package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.ResourceRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.ResourceResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/modules/{moduleId}/resources")
    public ResponseEntity<ResourceResponse> createResource(
            @PathVariable Long moduleId,
            @Valid @RequestBody ResourceRequest request
    ) {
        ResourceResponse response = resourceService.createResource(moduleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/modules/{moduleId}/resources")
    public ResponseEntity<?> getResourcesByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(resourceService.getResourcesByModule(moduleId));
    }

    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable Long resourceId) {
        return ResponseEntity.ok(resourceService.getResourceById(resourceId));
    }

    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<String> deleteResource(@PathVariable Long resourceId) {
        resourceService.deleteResource(resourceId);
        return ResponseEntity.ok("Resource deleted successfully");
    }
}