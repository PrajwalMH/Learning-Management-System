package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Course;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.DiscussionPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionPostRepository extends JpaRepository<DiscussionPost, Long> {

    List<DiscussionPost> findByCourseAndActiveTrueOrderByCreatedAtDesc(Course course);
}