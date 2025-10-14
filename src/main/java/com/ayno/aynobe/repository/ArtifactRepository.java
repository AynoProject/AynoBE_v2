package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Artifact;
import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    Page<Artifact> findByVisibility(VisibilityType visibility, Pageable pageable);
    Page<Artifact> findByVisibilityAndCategory(VisibilityType visibility, FlowType category, Pageable pageable);
    boolean existsBySlug(String slug);
    boolean existsBySlugAndArtifactIdNot(String slug, Long artifactId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Artifact a set a.likeCount = a.likeCount + :delta where a.artifactId = :artifactId")
    int updateLikeCount(@Param("artifactId") Long artifactId, @Param("delta") long delta);
}