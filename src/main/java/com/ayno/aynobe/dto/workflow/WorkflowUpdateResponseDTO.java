package com.ayno.aynobe.dto.workflow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowUpdateResponse")
public class WorkflowUpdateResponseDTO {
    private Long workflowId;
}
