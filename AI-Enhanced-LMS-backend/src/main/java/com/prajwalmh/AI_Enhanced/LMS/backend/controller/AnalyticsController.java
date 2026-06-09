package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.CourseAnalyticsResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.GradeDistributionResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.StudentTrendResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/courses/{courseId}/summary")
    public ResponseEntity<CourseAnalyticsResponse> getCourseSummary(@PathVariable Long courseId) {
        return ResponseEntity.ok(analyticsService.getCourseSummary(courseId));
    }

    @GetMapping("/courses/{courseId}/grade-distribution")
    public ResponseEntity<List<GradeDistributionResponse>> getGradeDistribution(@PathVariable Long courseId) {
        return ResponseEntity.ok(analyticsService.getGradeDistribution(courseId));
    }

    @GetMapping("/students/{studentId}/courses/{courseId}/trend")
    public ResponseEntity<List<StudentTrendResponse>> getStudentTrend(
            @PathVariable Long studentId,
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(analyticsService.getStudentTrend(studentId, courseId));
    }
}