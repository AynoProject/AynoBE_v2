package com.ayno.aynobe.dto.workflow;

import com.ayno.aynobe.entity.enums.FlowType;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowDetailResponse")
public class WorkflowDetailResponseDTO {
    private Long workflowId;
    private FlowType category;
    private String workflowTitle;
    private JsonNode canvasJson;

    private Long ownerId;
    private String ownerName;

    private List<StepDTO> steps;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StepDTO {
        private Long stepId;
        private int stepNo;
        private String stepTitle;
        private String stepContent;
        private Long toolId;          // null 가능
        private String toolName;      // null 가능
        private List<SectionDTO> sections;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SectionDTO {
        private Long sectionId;
        private int orderNo;
        private String sectionTitle;
        private String stepType;      // enum name
        private String promptRole;    // enum name
        private String stepContent;
        private String mediaUrl;
    }
}
