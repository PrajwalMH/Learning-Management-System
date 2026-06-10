package com.prajwalmh.AI_Enhanced.LMS.backend.service;

import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.DiscussionPostRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.request.DiscussionReplyRequest;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.DiscussionPostResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.dto.response.DiscussionReplyResponse;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.DiscussionPost;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.DiscussionReply;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.User;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.CourseRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.DiscussionPostRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.DiscussionReplyRepository;
import com.prajwalmh.AI_Enhanced.LMS.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionPostRepository discussionPostRepository;
    private final DiscussionReplyRepository discussionReplyRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public DiscussionPostResponse createPost(Long courseId, DiscussionPostRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        User postedBy = userRepository.findById(request.getPostedById())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getPostedById()));

        DiscussionPost post = DiscussionPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .course(course)
                .postedBy(postedBy)
                .active(true)
                .build();

        DiscussionPost savedPost = discussionPostRepository.save(post);

        return mapPostToResponse(savedPost, true);
    }

    public List<DiscussionPostResponse> getPostsByCourse(Long courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        return discussionPostRepository.findByCourseAndActiveTrueOrderByCreatedAtDesc(course)
                .stream()
                .map(post -> mapPostToResponse(post, false))
                .toList();
    }

    public DiscussionPostResponse getPostById(Long postId) {

        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Discussion post not found with id: " + postId));

        return mapPostToResponse(post, true);
    }

    public DiscussionPostResponse updatePost(Long postId, DiscussionPostRequest request) {

        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Discussion post not found with id: " + postId));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        DiscussionPost updatedPost = discussionPostRepository.save(post);

        return mapPostToResponse(updatedPost, true);
    }

    public void deactivatePost(Long postId) {

        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Discussion post not found with id: " + postId));

        post.setActive(false);
        discussionPostRepository.save(post);
    }

    public DiscussionReplyResponse addReply(Long postId, DiscussionReplyRequest request) {

        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Discussion post not found with id: " + postId));

        User repliedBy = userRepository.findById(request.getRepliedById())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getRepliedById()));

        DiscussionReply reply = DiscussionReply.builder()
                .content(request.getContent())
                .post(post)
                .repliedBy(repliedBy)
                .active(true)
                .build();

        DiscussionReply savedReply = discussionReplyRepository.save(reply);

        return mapReplyToResponse(savedReply);
    }

    public List<DiscussionReplyResponse> getRepliesByPost(Long postId) {

        DiscussionPost post = discussionPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Discussion post not found with id: " + postId));

        return discussionReplyRepository.findByPostAndActiveTrueOrderByCreatedAtAsc(post)
                .stream()
                .map(this::mapReplyToResponse)
                .toList();
    }

    public void deactivateReply(Long replyId) {

        DiscussionReply reply = discussionReplyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Discussion reply not found with id: " + replyId));

        reply.setActive(false);
        discussionReplyRepository.save(reply);
    }

    private DiscussionPostResponse mapPostToResponse(DiscussionPost post, boolean includeReplies) {

        Course course = post.getCourse();
        User postedBy = post.getPostedBy();

        List<DiscussionReply> replies = discussionReplyRepository
                .findByPostAndActiveTrueOrderByCreatedAtAsc(post);

        List<DiscussionReplyResponse> replyResponses = includeReplies
                ? replies.stream().map(this::mapReplyToResponse).toList()
                : null;

        return DiscussionPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .postedById(postedBy.getId())
                .postedByName(postedBy.getFullName())
                .postedByRole(postedBy.getRole().name())
                .active(post.isActive())
                .replyCount((long) replies.size())
                .replies(replyResponses)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private DiscussionReplyResponse mapReplyToResponse(DiscussionReply reply) {

        User repliedBy = reply.getRepliedBy();

        return DiscussionReplyResponse.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .postId(reply.getPost().getId())
                .repliedById(repliedBy.getId())
                .repliedByName(repliedBy.getFullName())
                .repliedByRole(repliedBy.getRole().name())
                .active(reply.isActive())
                .createdAt(reply.getCreatedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
    }
}