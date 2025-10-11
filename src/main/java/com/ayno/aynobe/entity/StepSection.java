package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.PromptType;
import com.ayno.aynobe.entity.enums.StepType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stepSection",
        indexes = {
                @Index(name = "idx_section_step", columnList = "stepId, orderNo"),
                @Index(name = "idx_section_kind", columnList = "stepType, promptRole")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepSection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stepId", nullable = false)
    private WorkflowStep workflowStep;

    @Column(name = "orderNo", nullable = false)
    private int orderNo;

    @Column(name = "sectionTitle", nullable = false, length = 100)
    private String sectionTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "stepType", nullable = false, length = 20)
    private StepType stepType;   // PROMPT / MEDIA / NOTE

    @Enumerated(EnumType.STRING)
    @Column(name = "promptRole", nullable = false, length = 20)
    private PromptType promptRole; // PROMPT일 때 의미 있음

    @Column(name = "stepContent", nullable = false, columnDefinition = "TEXT")
    private String stepContent;

    @Column(name = "mediaUrl", nullable = false, length = 512)
    private String mediaUrl;
}

