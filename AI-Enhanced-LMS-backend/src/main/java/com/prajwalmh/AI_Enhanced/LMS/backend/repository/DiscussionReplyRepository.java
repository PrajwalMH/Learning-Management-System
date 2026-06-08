package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.DiscussionPost;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.DiscussionReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionReplyRepository extends JpaRepository<DiscussionReply, Long> {

    List<DiscussionReply> findByPostAndActiveTrueOrderByCreatedAtAsc(DiscussionPost post);
}