package com.ayno.aynobe.dto.artifact;

import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import com.ayno.aynobe.entity.enums.MediaType;
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
@Schema(name = "ArtifactUpdateRequest")
public class ArtifactUpdateRequestDTO {

    @NotNull
    private FlowType category;

    @NotBlank @Size(max = 100)
    private String artifactTitle;

    @NotNull
    private VisibilityType visibility;

    @NotNull @Min(0) @Max(100)
    private Integer aiUsagePercent;

    @Builder.Default
    private Boolean isPremium = false;

    @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
    @Size(max = 512)
    private String thumbnailUrl;

    @NotBlank @Size(max = 256)
    private String slug;

    /** 연결 워크플로우(선택, null 가능) */
    private Long workflowId;

    /** 미디어 전체 교체(MVP: 전체 replace) */
    @Valid
    @Builder.Default
    private List<MediaDTO> medias = List.of();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "ArtifactUpdateMedia")
    public static class MediaDTO {
        @NotNull
        private MediaType mediaType;

        @NotBlank
        @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
        @Size(max = 1024)
        private String mediaUrl;
    }
}