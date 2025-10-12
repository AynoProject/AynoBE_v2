package com.ayno.aynobe.dto.workflow;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowCreateStep")
public class WorkflowCreateStepDTO {

    @Min(1)
    private int stepNo;

    @NotBlank @Size(max = 100)
    private String stepTitle;

    @NotBlank
    private String stepContent;

    @Schema(description = "사용할 툴 ID, 없으면 null")
    private Long toolId; // nullable

    @NotEmpty
    @Valid
    private List<WorkflowCreateSectionDTO> sections;
}
