package com.ayno.aynobe.repository;

import com.ayno.aynobe.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
}