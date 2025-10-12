package com.ayno.aynobe.entity;

import com.ayno.aynobe.dto.workflow.WorkflowCreateRequestDTO;
import com.ayno.aynobe.dto.workflow.WorkflowDetailResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "workflowStep")
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
    @JoinColumn(name = "toolId", nullable = true)
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

    /** 섹션 추가 (양방향 고정) */
    public StepSection addSection(WorkflowCreateRequestDTO.SectionDTO sectionDTO) {
        StepSection section = StepSection.builder()
                .workflowStep(this)
                .orderNo(sectionDTO.getOrderNo())
                .sectionTitle(sectionDTO.getSectionTitle())
                .sectionType(sectionDTO.getSectionType())
                .promptRole(sectionDTO.getPromptRole())
                .stepContent(sectionDTO.getStepContent())
                .mediaUrl(sectionDTO.getMediaUrl())
                .build();

        this.stepSections.add(section);
        return section;
    }

    /** sections diff 동기화 */
    public void syncSections(
            List<com.ayno.aynobe.dto.workflow.WorkflowUpdateRequestDTO.SectionDTO> newSections
    ) {
        Map<Long, StepSection> existed = this.stepSections.stream()
                .collect(java.util.stream.Collectors.toMap(StepSection::getSectionId, s -> s));

        List<StepSection> next = new ArrayList<>(newSections.size());

        for (var d : newSections) {
            StepSection sec;
            if (d.getSectionId() != null && existed.containsKey(d.getSectionId())) {
                // 수정
                sec = existed.remove(d.getSectionId());
                sec.setOrderNo(d.getOrderNo());
                sec.setSectionTitle(d.getSectionTitle());
                sec.setSectionType(d.getSectionType());
                sec.setPromptRole(d.getPromptRole());
                sec.setStepContent(d.getStepContent());
                sec.setMediaUrl(d.getMediaUrl());
            } else {
                // 추가
                sec = StepSection.builder()
                        .workflowStep(this)
                        .orderNo(d.getOrderNo())
                        .sectionTitle(d.getSectionTitle())
                        .sectionType(d.getSectionType())
                        .promptRole(d.getPromptRole())
                        .stepContent(d.getStepContent())
                        .mediaUrl(d.getMediaUrl())
                        .build();
            }
            next.add(sec);
        }

        // 삭제
        for (StepSection removed : existed.values()) {
            removed.setWorkflowStep(null);         // orphanRemoval=true → DELETE
        }

        // 교체(순서 반영)
        this.stepSections.clear();
        this.stepSections.addAll(next);
    }

    /** 전체 분리(삭제용) */
    public void detachAllSections() {
        for (StepSection s : new ArrayList<>(this.stepSections)) {
            s.setWorkflowStep(null);
        }
        this.stepSections.clear();
    }
}

