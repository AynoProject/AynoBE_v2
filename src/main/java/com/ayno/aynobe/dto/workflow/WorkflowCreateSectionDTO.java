package com.ayno.aynobe.dto.workflow;

import com.ayno.aynobe.entity.enums.PromptType;
import com.ayno.aynobe.entity.enums.SectionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowCreateSection")
public class WorkflowCreateSectionDTO {

    @Min(1)
    private int orderNo;

    @NotBlank @Size(max = 100)
    private String sectionTitle;

    @NotNull
    private SectionType sectionType;        // PROMPT / MEDIA / NOTE

    @NotNull
    private PromptType promptRole;    // PROMPT일 때 의미

    @NotBlank
    private String stepContent;

    @NotBlank @Size(max = 512)
    private String mediaUrl;
}
