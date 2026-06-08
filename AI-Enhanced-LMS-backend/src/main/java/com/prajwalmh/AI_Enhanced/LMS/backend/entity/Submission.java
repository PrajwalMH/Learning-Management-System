package com.prajwalmh.AI_Enhanced.LMS.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "submissions",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"assignment_id", "student_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000)
    private String answerText;

    private String fileName;

    private String fileType;

    private Long fileSize;

    @Column(length = 1000)
    private String fileUrl;

    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private LocalDateTime submittedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = SubmissionStatus.SUBMITTED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}