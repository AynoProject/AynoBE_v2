package com.ayno.aynobe.entity;

import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "artifact",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_artifact_slug", columnNames = "slug")
        },
        indexes = {
                @Index(name = "idx_artifact_user", columnList = "user_id"),
                @Index(name = "idx_artifact_workflow", columnList = "workflow_id"),
                @Index(name = "idx_artifact_visibility_created", columnList = "visibility, created_at")
        }
)
public class Artifact extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artifactId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflowId")
    private Workflow workflow;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FlowType category;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String artifactTitle;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isPremium = false;

    @Min(0) @Max(100)
    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer aiUsagePercent;

    @Builder.Default
    @Column(nullable = false)
    private Long viewCount = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long likeCount = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VisibilityType visibility;

    @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
    @Column(length = 512)
    private String thumbnailUrl;

    @NotBlank
    @Column(nullable = false, length = 256)
    private String slug;

    @Builder.Default
    @OneToMany(mappedBy = "artifact", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("mediaId ASC")
    private List<ArtifactMedia> medias = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (visibility == null) visibility = VisibilityType.PUBLIC;
        if (aiUsagePercent == null) aiUsagePercent = 0;
        if (isPremium == null) isPremium = false;
        if (viewCount == null) viewCount = 0L;
        if (likeCount == null) likeCount = 0L;
    }
}
