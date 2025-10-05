package com.ayno.aynobe.dto.interest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "관심요소 생성 DTO")
public class InterestCreateRequestDTO {
    @Schema(description = "관심요소", example = "영상 편집")
    @NotBlank
    private String label;
}
