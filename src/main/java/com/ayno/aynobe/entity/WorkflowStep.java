package com.ayno.aynobe.entity;

import com.ayno.aynobe.dto.workflow.WorkflowDetailResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "workflowStep",
        uniqueConstraints = @UniqueConstraint(name = "uq_step_workflow_seq",
                columnNames = {"workflowId","stepNo"}),
        indexes = @Index(name = "idx_step_workflow", columnList = "workflowId, stepNo"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStep extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflowId", nullable = false)
    private Workflow workflow;

    @Builder.Default
    @OneToMany(mappedBy = "workflowStep", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<StepSection> stepSections = new ArrayList<>();

    // 1부터 시작 권장
    @Column(name = "stepNo", nullable = false)
    private int stepNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "toolId", nullable = false)
    private Tool tool;

    @Column(name = "stepTitle", nullable = false, length = 100)
    private String stepTitle;

    @Column(name = "stepContent", nullable = false, columnDefinition = "TEXT")
    private String stepContent;

    public WorkflowDetailResponseDTO.StepDTO toDetailDTO() {
        return WorkflowDetailResponseDTO.StepDTO.builder()
                .stepId(this.stepId)
                .stepNo(this.stepNo)
                .stepTitle(this.stepTitle)
                .stepContent(this.stepContent)
                .toolId(this.tool != null ? this.tool.getToolId() : null)
                .toolName(this.tool != null ? this.tool.getToolName() : null)
                .sections(this.stepSections.stream()
                        .sorted(Comparator.comparingInt(StepSection::getOrderNo))
                        .map(StepSection::toDetailDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}

