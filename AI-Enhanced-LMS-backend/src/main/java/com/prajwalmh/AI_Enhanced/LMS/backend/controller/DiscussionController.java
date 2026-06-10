package com.prajwalmh.AI_Enhanced.LMS.backend.controller;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.DiscussionPostRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.DiscussionReplyRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.DiscussionPostResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.DiscussionReplyResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.service.DiscussionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping("/courses/{courseId}/discussions")
    public ResponseEntity<DiscussionPostResponse> createPost(
            @PathVariable Long courseId,
            @Valid @RequestBody DiscussionPostRequest request
    ) {
        DiscussionPostResponse response = discussionService.createPost(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/courses/{courseId}/discussions")
    public ResponseEntity<?> getPostsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(discussionService.getPostsByCourse(courseId));
    }

    @GetMapping("/discussions/{postId}")
    public ResponseEntity<DiscussionPostResponse> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(discussionService.getPostById(postId));
    }

    @PutMapping("/discussions/{postId}")
    public ResponseEntity<DiscussionPostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody DiscussionPostRequest request
    ) {
        return ResponseEntity.ok(discussionService.updatePost(postId, request));
    }

    @DeleteMapping("/discussions/{postId}")
    public ResponseEntity<String> deactivatePost(@PathVariable Long postId) {
        discussionService.deactivatePost(postId);
        return ResponseEntity.ok("Discussion post deactivated successfully");
    }

    @PostMapping("/discussions/{postId}/replies")
    public ResponseEntity<DiscussionReplyResponse> addReply(
            @PathVariable Long postId,
            @Valid @RequestBody DiscussionReplyRequest request
    ) {
        DiscussionReplyResponse response = discussionService.addReply(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/discussions/{postId}/replies")
    public ResponseEntity<?> getRepliesByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(discussionService.getRepliesByPost(postId));
    }

    @DeleteMapping("/discussions/replies/{replyId}")
    public ResponseEntity<String> deactivateReply(@PathVariable Long replyId) {
        discussionService.deactivateReply(replyId);
        return ResponseEntity.ok("Discussion reply deactivated successfully");
    }
}