package com.prajwalmh.AI_Enhanced.LMS.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressUpdateRequest {

    @Min(value = 0, message = "Completed modules cannot be negative")
    private Integer completedModules;

    @Min(value = 0, message = "Total modules cannot be negative")
    private Integer totalModules;

    @Min(value = 0, message = "Progress percentage cannot be less than 0")
    @Max(value = 100, message = "Progress percentage cannot be more than 100")
    private Double progressPercentage;

    @Min(value = 0, message = "Average score cannot be less than 0")
    @Max(value = 100, message = "Average score cannot be more than 100")
    private Double averageScore;
}