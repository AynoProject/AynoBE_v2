package com.ayno.aynobe.dto.workflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowDeleteResponse")
public class WorkflowDeleteResponseDTO {
    @Schema(description = "삭제된 워크플로우 ID", example = "123")
    private Long workflowId;
}