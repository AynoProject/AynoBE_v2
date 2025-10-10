package com.ayno.aynobe.dto.tool;

import com.ayno.aynobe.entity.Tool;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ToolCreateResponseDTO {
    private Long toolId;
    private String toolName;

    public static ToolCreateResponseDTO from(Tool tool) {
        return ToolCreateResponseDTO.builder()
                .toolId(tool.getToolId())
                .toolName(tool.getToolName())
                .build();
    }
}
