package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflow",
        indexes = {
                @Index(name = "idx_workflow_owner", columnList = "userId"),
                @Index(name = "idx_workflow_rank",  columnList = "likeCount, viewCount")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workflowId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private FlowType category;

    @Column(name = "workflowTitle", nullable = false, length = 100)
    private String workflowTitle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "viewCount", nullable = false)
    private long viewCount = 0;

    @Column(name = "likeCount", nullable = false)
    private long likeCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 20)
    private VisibilityType visibility = VisibilityType.PUBLIC;

    @Column(name = "thumbnailUrl", length = 512, nullable = false)
    private String thumbnailUrl;

    // JSON
    @JdbcTypeCode(SqlTypes.JSON)             // Hibernate 6
    @Column(name = "canvasJson", columnDefinition = "json")
    private JsonNode canvasJson;

    @Column(name = "slug", length = 256, nullable = false)
    private String slug;

    @Builder.Default
    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowStep> workflowSteps = new ArrayList<>();
}

