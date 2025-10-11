package com.ayno.aynobe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
}

