package com.ayno.aynobe.entity;

import com.ayno.aynobe.dto.artifact.ArtifactCreateRequestDTO;
import com.ayno.aynobe.dto.artifact.ArtifactUpdateRequestDTO;
import com.ayno.aynobe.entity.enums.MediaType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "artifact_media",
        indexes = {
                @Index(name = "idx_media_artifact", columnList = "artifact_id")
        }
)
public class ArtifactMedia extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artifactId", nullable = false)
    private Artifact artifact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaType mediaType;

    @NotBlank
    @Pattern(regexp = "^https://.+", message = "HTTPS URL만 허용됩니다.")
    @Column(nullable = false, length = 1024)
    private String mediaUrl;

    public void assignArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public static ArtifactMedia from(ArtifactCreateRequestDTO.MediaDTO dto) {
        return ArtifactMedia.builder()
                .mediaType(dto.getMediaType())
                .mediaUrl(dto.getMediaUrl())
                .build();
    }


    public static ArtifactMedia from(ArtifactUpdateRequestDTO.MediaDTO dto) {
        return ArtifactMedia.builder()
                .mediaType(dto.getMediaType())
                .mediaUrl(dto.getMediaUrl())
                .build();
    }
}
