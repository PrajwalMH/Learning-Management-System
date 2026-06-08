package com.prajwalmh.AI_Enhanced.LMS.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "quiz_attempts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"quiz_id", "student_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    private Integer totalQuestions;

    private Integer correctAnswers;

    @Column(length = 5000)
    private String submittedAnswersJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private LocalDateTime attemptedAt;

    @PrePersist
    protected void onCreate() {
        this.attemptedAt = LocalDateTime.now();
    }
}