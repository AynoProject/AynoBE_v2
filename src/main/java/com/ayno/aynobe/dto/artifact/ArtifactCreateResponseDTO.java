package com.ayno.aynobe.dto.artifact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ArtifactCreateResponse")
public class ArtifactCreateResponseDTO {
    private Long artifactId;
}
