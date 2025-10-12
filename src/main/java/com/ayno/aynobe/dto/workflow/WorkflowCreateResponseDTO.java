package com.ayno.aynobe.dto.workflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowCreateResponse")
public class WorkflowCreateResponseDTO {
    @Schema(description = "생성된 워크플로우 ID", example = "123")
    private Long workflowId;
}