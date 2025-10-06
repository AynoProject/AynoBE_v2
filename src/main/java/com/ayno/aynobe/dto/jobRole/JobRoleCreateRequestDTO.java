package com.ayno.aynobe.dto.jobRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "직무 생성 DTO")
public class JobRoleCreateRequestDTO {
    @Schema(description = "직무 라벨", example = "Engineering")
    @NotBlank
    private String jobRoleLabel;
}