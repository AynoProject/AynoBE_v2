package com.ayno.aynobe.dto.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewLogDetailResponseDTO {

    @Schema(description = "뷰 로그 PK", example = "12345")
    private Long viewId;

    @Schema(description = "아티팩트 ID", example = "678")
    private Long artifactId;

    @Schema(description = "사용자 ID", example = "42")
    private Long userId;

    @Schema(description = "진입 시각", example = "2025-10-15T12:34:56")
    private LocalDateTime enteredAt;

    @Schema(description = "체류 시간(초)", example = "120")
    private Long dwellSec;
}