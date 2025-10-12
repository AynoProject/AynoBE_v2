package com.ayno.aynobe.dto.workflow;


import com.ayno.aynobe.entity.enums.FlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "WorkflowCard")
public class WorkflowCardDTO {
    private Long workflowId;
    private String workflowTitle;
    private String thumbnailUrl;
    private long likeCount;
    private long viewCount;
    private FlowType category;

    // 카드에 필요한 최소 사용자 정보
    private Long ownerId;
    private String ownerName;
}