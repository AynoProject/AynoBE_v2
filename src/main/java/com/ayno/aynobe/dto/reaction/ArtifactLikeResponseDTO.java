package com.ayno.aynobe.dto.reaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ArtifactLikeResponse")
public class ArtifactLikeResponseDTO {
    @Schema(description = "결과물 ID", example = "1")
    private Long artifactId;

    @Schema(description = "해당 사용자가 좋아요 눌렀는지", example = "true")
    private boolean liked;

    @Schema(description = "전체 좋아요 수", example = "42")
    private Long likeCount;
}
