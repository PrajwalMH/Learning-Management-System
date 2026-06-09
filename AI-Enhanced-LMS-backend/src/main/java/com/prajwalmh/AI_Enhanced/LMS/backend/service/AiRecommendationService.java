package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.AiRecommendationRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.AiRecommendationResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.AiRecommendation;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.AiRecommendationRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {

    private final AiRecommendationRepository aiRecommendationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    private final WebClient webClient = WebClient.builder().build();

    @Value("${serpapi.api.key}")
    private String serpApiKey;

    @Value("${serpapi.base.url}")
    private String serpApiBaseUrl;

    public List<AiRecommendationResponse> generateRecommendations(AiRecommendationRequest request) {

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        if (student.getRole() != Role.STUDENT) {
            throw new RuntimeException("Selected user is not a student");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        String priority = calculatePriority(request.getScore());

        String searchQuery = buildSearchQuery(request.getWeakTopic());

        Map response = webClient.get()
                .uri(serpApiBaseUrl + "?engine=google&q={query}&api_key={apiKey}",
                        searchQuery,
                        serpApiKey)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<ScoredResource> scoredResources = new ArrayList<>();

        if (response != null && response.get("organic_results") instanceof List<?> organicResults) {

            for (Object resultObject : organicResults) {

                if (!(resultObject instanceof Map<?, ?> resultMap)) {
                    continue;
                }

                String title = getValue(resultMap, "title");
                String link = getValue(resultMap, "link");
                String snippet = getValue(resultMap, "snippet");

                if (link.isBlank()) {
                    continue;
                }

                if (isBlockedSource(link)) {
                    continue;
                }

                int recommendationScore = calculateRecommendationScore(
                        title,
                        link,
                        snippet,
                        request.getWeakTopic()
                );

                scoredResources.add(new ScoredResource(
                        title,
                        link,
                        snippet,
                        recommendationScore
                ));
            }
        }

        List<ScoredResource> topResources = scoredResources.stream()
                .sorted(Comparator.comparingInt(ScoredResource::getRecommendationScore).reversed())
                .limit(5)
                .toList();

        List<AiRecommendation> savedRecommendations = new ArrayList<>();

        for (ScoredResource resource : topResources) {

            AiRecommendation recommendation = AiRecommendation.builder()
                    .student(student)
                    .course(course)
                    .weakTopic(request.getWeakTopic())
                    .score(request.getScore())
                    .priority(priority)
                    .searchQuery(searchQuery)
                    .resourceTitle(resource.getTitle())
                    .recommendationText(resource.getSnippet())
                    .resourceUrl(resource.getLink())
                    .resourceType(determineResourceType(resource.getLink()))
                    .recommendationScore(resource.getRecommendationScore())
                    .completed(false)
                    .build();

            savedRecommendations.add(aiRecommendationRepository.save(recommendation));
        }

        return savedRecommendations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AiRecommendationResponse> getRecommendationsByStudent(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        return aiRecommendationRepository.findByStudentOrderByGeneratedAtDesc(student)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<AiRecommendationResponse> getRecommendationsByStudentAndCourse(Long studentId, Long courseId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        return aiRecommendationRepository.findByStudentAndCourseOrderByGeneratedAtDesc(student, course)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private String buildSearchQuery(String weakTopic) {
        return weakTopic + " official documentation tutorial university guide examples";
    }

    private boolean isBlockedSource(String url) {

        String lowerUrl = url.toLowerCase();

        List<String> blockedDomains = List.of(
                "reddit.com",
                "quora.com",
                "stackoverflow.com/questions",
                "stackexchange.com",
                "medium.com",
                "dev.to",
                "hashnode.dev",
                "forum",
                "forums",
                "community",
                "discuss",
                "facebook.com",
                "twitter.com",
                "x.com",
                "linkedin.com/pulse"
        );

        return blockedDomains.stream().anyMatch(lowerUrl::contains);
    }

    private int calculateRecommendationScore(String title, String link, String snippet, String weakTopic) {

        int score = 0;

        String lowerTitle = title.toLowerCase();
        String lowerLink = link.toLowerCase();
        String lowerSnippet = snippet.toLowerCase();
        String lowerTopic = weakTopic.toLowerCase();

        if (lowerLink.contains("docs.spring.io")
                || lowerLink.contains("developer.mozilla.org")
                || lowerLink.contains("docs.oracle.com")
                || lowerLink.contains("learn.microsoft.com")
                || lowerLink.contains("cloud.google.com/docs")
                || lowerLink.contains("aws.amazon.com/documentation")
                || lowerLink.contains("docs.github.com")) {
            score += 50;
        }

        if (lowerLink.contains(".edu")) {
            score += 40;
        }

        if (lowerLink.contains("baeldung.com")
                || lowerLink.contains("geeksforgeeks.org")
                || lowerLink.contains("tutorialspoint.com")
                || lowerLink.contains("w3schools.com")
                || lowerLink.contains("freecodecamp.org")
                || lowerLink.contains("coursera.org")
                || lowerLink.contains("edx.org")
                || lowerLink.contains("khanacademy.org")) {
            score += 35;
        }

        if (lowerLink.contains("spring.io")
                || lowerLink.contains("oracle.com")
                || lowerLink.contains("mysql.com")
                || lowerLink.contains("postgresql.org")
                || lowerLink.contains("mozilla.org")
                || lowerLink.contains("microsoft.com")
                || lowerLink.contains("amazon.com")
                || lowerLink.contains("google.com")) {
            score += 25;
        }

        String[] topicWords = lowerTopic.split("\\s+");

        for (String word : topicWords) {
            if (word.length() < 3) {
                continue;
            }

            if (lowerTitle.contains(word)) {
                score += 5;
            }

            if (lowerSnippet.contains(word)) {
                score += 3;
            }
        }

        if (lowerTitle.contains("tutorial")
                || lowerTitle.contains("guide")
                || lowerTitle.contains("documentation")
                || lowerTitle.contains("examples")
                || lowerTitle.contains("learn")) {
            score += 10;
        }

        return score;
    }

    private String determineResourceType(String link) {

        String lowerLink = link.toLowerCase();

        if (lowerLink.contains("docs.") || lowerLink.contains("/docs/")) {
            return "OFFICIAL_DOCUMENTATION";
        }

        if (lowerLink.contains(".edu")) {
            return "UNIVERSITY_RESOURCE";
        }

        if (lowerLink.contains("coursera.org") || lowerLink.contains("edx.org")) {
            return "COURSE";
        }

        if (lowerLink.contains("youtube.com") || lowerLink.contains("youtu.be")) {
            return "VIDEO";
        }

        return "WEB_RESOURCE";
    }

    private String getValue(Map<?, ?> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }

    private String calculatePriority(Double score) {
        if (score == null) {
            return "MEDIUM";
        }

        if (score < 50) {
            return "HIGH";
        } else if (score < 70) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    private AiRecommendationResponse mapToResponse(AiRecommendation recommendation) {

        User student = recommendation.getStudent();
        Course course = recommendation.getCourse();

        return AiRecommendationResponse.builder()
                .id(recommendation.getId())
                .studentId(student.getId())
                .studentName(student.getFullName())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .weakTopic(recommendation.getWeakTopic())
                .score(recommendation.getScore())
                .priority(recommendation.getPriority())
                .searchQuery(recommendation.getSearchQuery())
                .resourceTitle(recommendation.getResourceTitle())
                .recommendationText(recommendation.getRecommendationText())
                .resourceUrl(recommendation.getResourceUrl())
                .resourceType(recommendation.getResourceType())
                .recommendationScore(recommendation.getRecommendationScore())
                .completed(recommendation.isCompleted())
                .generatedAt(recommendation.getGeneratedAt())
                .build();
    }

    @Getter
    @AllArgsConstructor
    private static class ScoredResource {
        private String title;
        private String link;
        private String snippet;
        private int recommendationScore;
    }
}