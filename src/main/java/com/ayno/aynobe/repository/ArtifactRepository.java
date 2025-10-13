package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Artifact;
import com.ayno.aynobe.entity.enums.FlowType;
import com.ayno.aynobe.entity.enums.VisibilityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    Page<Artifact> findByVisibility(VisibilityType visibility, Pageable pageable);
    Page<Artifact> findByVisibilityAndCategory(VisibilityType visibility, FlowType category, Pageable pageable);
}