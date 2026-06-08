package com.prajwalmh.AI_Enhanced.LMS.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "course_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer completedModules;

    private Integer totalModules;

    private Double progressPercentage;

    private Double averageScore;

    private LocalDateTime lastAccessedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.completedModules == null) {
            this.completedModules = 0;
        }

        if (this.totalModules == null) {
            this.totalModules = 0;
        }

        if (this.progressPercentage == null) {
            this.progressPercentage = 0.0;
        }

        if (this.averageScore == null) {
            this.averageScore = 0.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}