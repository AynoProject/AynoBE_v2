package com.ayno.aynobe.dto.tool;

import com.ayno.aynobe.entity.Tool;
import com.ayno.aynobe.entity.enums.ToolType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
public class ToolListItemResponseDTO {
    @Schema(description = "툴 PK", example = "1")
    private Long toolId;

    @Schema(description = "툴 타입", example = "AI")
    private ToolType toolType;

    @Schema(description = "툴 이름", example = "ChatGPT")
    private String toolName;

    @Schema(description = "아이콘 URL", example = "https://cdn.example.com/icons/chatgpt.png")
    private String toolIconUrl;

    @Schema(description = "공식 사이트 URL", example = "https://chat.openai.com")
    private String toolSiteUrl;

    public static ToolListItemResponseDTO from(Tool t) {
        return ToolListItemResponseDTO.builder()
                .toolId(t.getToolId())
                .toolType(t.getToolType())
                .toolName(t.getToolName())
                .toolIconUrl(t.getToolIconUrl())
                .toolSiteUrl(t.getToolSiteUrl())
                .build();
    }
}