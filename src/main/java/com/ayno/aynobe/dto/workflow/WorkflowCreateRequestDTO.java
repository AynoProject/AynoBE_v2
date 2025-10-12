package com.ayno.aynobe.dto.workflow;

import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import com.fasterxml.jackson.databind.JsonNode;
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
@Schema(name = "WorkflowCreateRequest")
public class WorkflowCreateRequestDTO {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private FlowType category;

    @NotBlank @Size(max = 100)
    @Schema(example = "유튜브 썸네일 자동 생성")
    private String workflowTitle;

    @NotNull
    private VisibilityType visibility;

    @NotBlank @Size(max = 512)
    private String thumbnailUrl;

    @Schema(description = "캔버스 JSON 객체 예: {\"nodes\":[],\"edges\":[]}")
    private JsonNode canvasJson;

    @NotBlank @Size(max = 256)
    private String slug;

    @NotEmpty
    @Valid
    private List<WorkflowCreateStepDTO> steps;
}
