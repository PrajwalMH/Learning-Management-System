package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.AiRecommendationRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.AiRecommendationResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.AiRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/recommendations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping("/generate")
    public ResponseEntity<List<AiRecommendationResponse>> generateRecommendations(
            @Valid @RequestBody AiRecommendationRequest request
    ) {
        List<AiRecommendationResponse> response =
                aiRecommendationService.generateRecommendations(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AiRecommendationResponse>> getRecommendationsByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(aiRecommendationService.getRecommendationsByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<AiRecommendationResponse>> getRecommendationsByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                aiRecommendationService.getRecommendationsByStudentAndCourse(studentId, courseId)
        );
    }
}