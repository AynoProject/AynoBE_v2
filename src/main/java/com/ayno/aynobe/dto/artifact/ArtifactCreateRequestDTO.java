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
@Schema(name = "ArtifactCreateRequest")
public class ArtifactCreateRequestDTO {
    @NotNull
    private FlowType category;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "결과물 제목", example = "썸네일 10분 자동 제작")
    private String artifactTitle;

    @NotNull
    private VisibilityType visibility; // PUBLIC | PRIVATE

    @NotNull
    @Min(0) @Max(100)
    private Integer aiUsagePercent;

    @Builder.Default
    private Boolean isPremium = false;

    @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
    @Size(max = 512)
    private String thumbnailUrl;

    @NotBlank
    @Size(max = 256)
    private String slug;

    /** 연결 워크플로우 (선택) */
    private Long workflowId;

    /** 미디어 목록 (선택) */
    @Valid
    @Builder.Default
    private List<MediaDTO> medias = List.of();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(name = "ArtifactCreateMedia")
    public static class MediaDTO {

        @NotNull
        private MediaType mediaType; // IMAGE | VIDEO | FILE

        @NotBlank
        @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
        @Size(max = 1024)
        private String mediaUrl;
    }
}
