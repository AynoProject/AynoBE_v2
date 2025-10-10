package com.ayno.aynobe.dto.tool;

import com.ayno.aynobe.entity.enums.ToolType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolCreateRequestDTO {

    @Schema(description = "툴 타입", example = "AI")
    @NotNull
    private ToolType toolType;

    @Schema(description = "툴 이름", example = "ChatGPT")
    @NotBlank
    @Size(max = 100)
    private String toolName;

    @Schema(description = "툴 아이콘 URL", example = "https://cdn.example.com/icons/chatgpt.png")
    @NotBlank
    @Size(max = 512)
    private String toolIconUrl;

    @Schema(description = "툴 공식 사이트 URL", example = "https://chat.openai.com")
    @NotBlank
    @Size(max = 512)
    @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
    private String toolSiteUrl;
}
