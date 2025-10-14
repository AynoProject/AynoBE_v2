package com.ayno.aynobe.dto.artifact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ArtifactUpdateResponse")
public class ArtifactUpdateResponseDTO {
    private Long artifactId;
}