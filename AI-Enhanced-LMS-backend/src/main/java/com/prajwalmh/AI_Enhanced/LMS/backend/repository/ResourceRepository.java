package com.prajwalmh.AI_Enhanced.LMS.backend.repository;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.CourseModule;
import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByModule(CourseModule module);

    List<Resource> findByModuleOrderByUploadedAtDesc(CourseModule module);
}