package com.ayno.aynobe.dto.reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowLikeResponse")
public class WorkflowLikeResponseDTO {
    @Schema(description = "워크플로우 ID", example = "123")
    private Long workflowId;

    @Schema(description = "해당 사용자가 좋아요 눌렀는지", example = "true")
    private boolean liked;

    @Schema(description = "전체 좋아요 수", example = "42")
    private long likeCount;
}
