package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GradeRequest {

    @NotNull(message = "Marks obtained is required")
    private Double marksObtained;

    private String feedback;

    @NotNull(message = "Teacher ID is required")
    private Long gradedById;
}