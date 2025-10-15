package com.ayno.aynobe.dto.log;

import com.ayno.aynobe.entity.log.ViewLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 단일 ViewLog 응답 DTO
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ViewLogResponseDTO {

    @Schema(description = "뷰 로그 PK(비로그인은 null)", example = "9876")
    private Long viewId;

    // ====== Mapper ======
    public static ViewLogResponseDTO ofId(Long id) {
        return ViewLogResponseDTO.builder().viewId(id).build();
    }

    public static ViewLogResponseDTO from(ViewLog v) {
        return ofId(v.getViewId());
    }
}