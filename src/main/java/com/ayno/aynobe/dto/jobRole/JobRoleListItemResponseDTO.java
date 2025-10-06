package com.ayno.aynobe.dto.jobRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "직무 리스트 아이템 DTO")
public class JobRoleListItemResponseDTO {
    @Schema(description = "직무 ID", example = "1")
    private Integer jobRoleId;
    @Schema(description = "직무 라벨", example = "Engineering")
    private String jobRoleLabel;
}
