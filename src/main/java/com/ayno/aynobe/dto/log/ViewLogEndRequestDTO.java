package com.ayno.aynobe.dto.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 조회 종료(이탈) 요청 DTO
 * - dwellSec 계산용. exitedAt 미지정 시 서버 시각 사용
 * - 서버는 (artifactId, userId, visitDate)로 기존 레코드 찾아 업데이트
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ViewLogEndRequestDTO {
    @Schema(description = "결과물 ID", example = "123")
    @NotNull
    private Long artifactId;
}